DROP SCHEMA IF EXISTS filmdatabase;

CREATE SCHEMA IF NOT EXISTS filmdatabase;
USE filmdatabase;

CREATE TABLE screenplayMedium (
  mediumID int NOT NULL AUTO_INCREMENT,
  mediumnavn varchar(45) DEFAULT NULL,
  PRIMARY KEY (mediumID)
);

INSERT INTO screenplayMedium (mediumnavn) VALUES ('Kino'),('TV'),('Streaming');

CREATE TABLE screenplay (
  screenplayID int NOT NULL AUTO_INCREMENT,
  tittel varchar(30) DEFAULT NULL,
  mediumID int DEFAULT NULL,
  selskapID int DEFAULT NULL,
  produksjonsår int DEFAULT NULL,
  dato int DEFAULT NULL,
  lengde int DEFAULT NULL,
  videoutgivelse int DEFAULT NULL,
  storyline varchar(100) DEFAULT NULL,
  komponist varchar(45) DEFAULT NULL,
  fremførelse varchar(45) DEFAULT NULL,
  sesongID int DEFAULT NULL,
  PRIMARY KEY (screenplayID),
  FOREIGN KEY (mediumID) REFERENCES screenplayMedium(mediumID)
);

CREATE TABLE person (
  PersonID int NOT NULL AUTO_INCREMENT,
  Navn varchar(30) DEFAULT NULL,
  Fødselsår int DEFAULT NULL,
  Fødselsland varchar(45) DEFAULT NULL,
  PRIMARY KEY (PersonID)
);

CREATE TABLE forfatterifilm (
  screenplayID int NOT NULL,
  personID int NOT NULL,
  PRIMARY KEY (screenplayID, personID),
  FOREIGN KEY (screenplayID) REFERENCES screenplay (screenplayID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (personID) REFERENCES person(personID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE regissørifilm (
  screenplayID int NOT NULL,
  personID int NOT NULL,
  PRIMARY KEY (screenplayID, personID),
  FOREIGN KEY (screenplayID) REFERENCES screenplay (screenplayID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (personID) REFERENCES person(personID) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE skuespillerifilm (
  screenplayID int NOT NULL,
  personID int NOT NULL,
  rolle varchar(30) DEFAULT NULL,
  PRIMARY KEY (screenplayID, personID),
  FOREIGN KEY (screenplayID) REFERENCES screenplay (screenplayID) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (personID) REFERENCES person(personID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE serie (
  serieID int NOT NULL AUTO_INCREMENT,
  serienavn varchar(45) NOT NULL,
  PRIMARY KEY (serieID)
);

CREATE TABLE sesong (
  sesongID int NOT NULL AUTO_INCREMENT,
  serieID int NOT NULL,
  sesongnavn varchar(45) DEFAULT NULL,
  PRIMARY KEY (sesongID),
  FOREIGN KEY (serieID) REFERENCES serie(serieID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE selskap (
  selskapID int NOT NULL AUTO_INCREMENT,
  navn varchar(45) DEFAULT NULL,
  adresse varchar(45) DEFAULT NULL,
  land varchar(45) DEFAULT NULL,
  url varchar(45) DEFAULT NULL,
  PRIMARY KEY (selskapID)
);

CREATE TABLE sjanger (
  sjangerID int NOT NULL AUTO_INCREMENT,
  sjangernavn varchar(30) DEFAULT NULL,
  PRIMARY KEY (sjangerID)
);

CREATE TABLE harSjanger (
  sjangerID int NOT NULL,
  screenplayID int NOT NULL,
  PRIMARY KEY (sjangerID, screenplayID),
  foreign key (sjangerID) references sjanger(sjangerID) on update cascade on delete cascade,
  foreign key (screenplayID) references screenplay(screenplayID) on update cascade on delete cascade
);

CREATE TABLE anmeldelse (
  anmeldelseID int NOT NULL AUTO_INCREMENT,
  screenplayID int NOT NULL,
  brukerID int DEFAULT NULL,
  tekst varchar(100) DEFAULT NULL,
  rating varchar(45) DEFAULT NULL,
  PRIMARY KEY (anmeldelseID),
  FOREIGN KEY (screenplayID) REFERENCES screenplay(screenplayID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE bruker (
  brukerID int NOT NULL AUTO_INCREMENT,
  brukernavn varchar(45) DEFAULT NULL,
  PRIMARY KEY (brukerID)
);
