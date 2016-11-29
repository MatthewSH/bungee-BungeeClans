CREATE TABLE `uuid_cache` ( 
	`id` Int( 255 ) AUTO_INCREMENT NOT NULL,
	`name` VarChar( 255 ) NOT NULL,
	`uuid` VarChar( 255 ) NOT NULL,
	PRIMARY KEY ( `id` ) )
ENGINE = InnoDB
AUTO_INCREMENT = 1;
CREATE TABLE `memberships` ( 
	`id` Int( 255 ) AUTO_INCREMENT NOT NULL,
	`uuid` VarChar( 255 ) NOT NULL,
	`clan-id` Int( 255 ) NOT NULL,
	`type` VarChar( 255 ) NOT NULL,
	`created_at` Date NOT NULL,
	PRIMARY KEY ( `id` ),
	CONSTRAINT `unique_id` UNIQUE( `id` ) )
ENGINE = InnoDB
AUTO_INCREMENT = 1;
CREATE TABLE `clans` ( 
	`id` Int( 255 ) AUTO_INCREMENT NOT NULL,
	`hash` VarChar( 255 ) NOT NULL,
	`tag` VarChar( 255 ) NOT NULL,
	`name` VarChar( 255 ) NOT NULL,
	`description` VarChar( 255 ) NOT NULL,
	`motto` VarChar( 255 ) NOT NULL,
	`owner` VarChar( 255 ) NOT NULL,
	`created_at` Date NOT NULL,
	PRIMARY KEY ( `id` ),
	CONSTRAINT `unique_id` UNIQUE( `id` ),
	CONSTRAINT `unique_tag` UNIQUE( `tag` ) )
ENGINE = InnoDB
AUTO_INCREMENT = 1;
ALTER TABLE `memberships`
	ADD CONSTRAINT `lnk_clans_memberships` FOREIGN KEY ( `clan-id` )
	REFERENCES `clans`( `id` )
	ON DELETE No Action
	ON UPDATE No Action;