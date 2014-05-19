CREATE TABLE Phones (
	id VARCHAR(30),
	PRIMARY KEY (id) 
);

CREATE TABLE Aps (
	bssid VARCHAR(17),
	ssid VARCHAR(20),
	latitude FLOAT,
	longitude FLOAT,
	PRIMARY KEY (bssid)
);

CREATE TABLE Logs (
	time VARCHAR(30),
	id VARCHAR(30) REFERENCES Phones(id),
	bssid VARCHAR(17) REFERENCES Aps(bssid),
	link INTEGER,
	level INTEGER,
	noise INTEGER,
	PRIMARY KEY (time, id, bssid)
);