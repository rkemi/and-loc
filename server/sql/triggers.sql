CREATE TRIGGER LogTrigger 
    INSTEAD OF INSERT ON logs
    REFERENCING NEW AS newlog
    FOR EACH ROW


CREATE OR REPLACE TRIGGER RegisterTrigger
  INSTEAD OF INSERT ON Registrations
  REFERENCING NEW AS newreg
  FOR EACH ROW

  DECLARE 
    alreadyFinished INT;
    alreadyRegistered INT;
    finishedPre INT;
    requiredPre INT;
    alreadyInQueue INT;
    maxStudents INT;
    noRegistered INT;
    inQueue INT;

  BEGIN
    
    SELECT COUNT(*) INTO alreadyFinished
      FROM PassedCourses p
      WHERE p.code = :newreg.code
      AND   p.id = :newreg.id;

    SELECT COUNT(code) INTO alreadyRegistered
      FROM Registered r
      WHERE r.code = :newreg.code
      AND   r.id = :newreg.id;

    SELECT COUNT(*) INTO finishedPre
      FROM PassedCourses p 
      WHERE p.id = :newreg.id
      AND   p.code = ANY (SELECT pre FROM Prerequisites
                            WHERE code = :newreg.code);

    SELECT COUNT(*) INTO requiredPre
      FROM Prerequisites pre
      WHERE pre.code = :newreg.code;

    SELECT COUNT(*) INTO alreadyInQueue
      FROM Queues q
      WHERE q.code = :newreg.code
      AND q.id = :newreg.id;

    SELECT MAX(maxstudents) INTO maxStudents
      FROM RestrictedCourses rc
      WHERE rc.code = :newreg.code;

    SELECT COUNT(*) INTO noRegistered
      FROM Registered r
      WHERE r.code = :newreg.code; 

    SELECT NVL(MAX(queuenumber),0) INTO inQueue
      FROM Queues q
      WHERE q.code = :newreg.code;

    IF (alreadyFinished = 1) THEN
      RAISE_APPLICATION_ERROR(-20000, 'Already finished course');
    ELSIF (alreadyRegistered = 1) THEN
      RAISE_APPLICATION_ERROR(-20001, 'Already registered');
    ELSIF (finishedPre != requiredPre) THEN
      RAISE_APPLICATION_ERROR(-20002, 'Lacking prerequisites');
    ELSIF (alreadyInQueue = 1) THEN
      RAISE_APPLICATION_ERROR(-20003, 'Already Waiting');
    ELSE
      IF (maxStudents IS NULL OR noRegistered < maxStudents) THEN
        INSERT INTO Registered
          VALUES (:newreg.id, :newreg.code);
      ELSIF (inQueue IS NULL) THEN
        INSERT INTO Queues
          VALUES (:newreg.id, :newreg.code, 1);
      ELSE
        INSERT INTO Queues
          VALUES (:newreg.id, :newreg.code, inQueue + 1);
      END IF;
    END IF;
  END;
/

CREATE OR REPLACE TRIGGER UnRegisterTrigger
  INSTEAD OF DELETE ON Registrations
  REFERENCING OLD as oldreg

  DECLARE 
    isregistered INT; 
    maxStudents INT;
    noRegistered INT;
    firstInQueue INT;

  BEGIN
    SELECT COUNT(*) INTO isregistered
    FROM Registered r
     WHERE r.id = :oldreg.id and r.code = :oldreg.code;

    SELECT MAX(maxstudents) INTO maxStudents
      FROM RestrictedCourses rc
      WHERE rc.code = :oldreg.code;

   SELECT MIN(id) INTO firstInQueue
      FROM CourseQueuePositions q
      WHERE q.code = :oldreg.code
      AND ROWNUM <= 1;

    IF (:oldreg.status = 'Waiting') THEN
      DELETE FROM Queues q
        WHERE q.code = :oldreg.code AND q.id = :oldreg.id;
    ELSE 
      DELETE FROM Registered r
        WHERE r.id = :oldreg.id AND r.code = :oldreg.code;
      SELECT COUNT(*) INTO noRegistered
      FROM Registered r
      WHERE r.code = :oldreg.code; 

      IF (maxStudents IS NOT NULL) THEN
        IF (noRegistered < maxStudents AND firstInQueue IS NOT NULL) THEN
          DELETE FROM Queues q
            WHERE q.id = firstInQueue AND q.code = :oldreg.code;
          INSERT INTO Registrations
            VALUES (firstInQueue, :oldreg.code, 'Registered');
        END IF;
      END IF;
    END IF;
  END;
/

