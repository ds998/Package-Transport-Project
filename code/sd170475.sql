
CREATE TABLE [Administrator]
( 
	[adminName]          varchar(100)  NOT NULL 
)
go

CREATE TABLE [City]
( 
	[idCity]             integer  IDENTITY  NOT NULL ,
	[name]               varchar(100)  NOT NULL ,
	[postalCode]         varchar(100)  NOT NULL 
)
go

CREATE TABLE [Courier]
( 
	[courierUserName]    varchar(100)  NOT NULL ,
	[licencePlateNumber] varchar(100)  NOT NULL ,
	[statusOfCourier]    integer  NULL 
	CONSTRAINT [CourierStatusTypeValues_2052829421]
		CHECK  ( statusOfCourier BETWEEN 0 AND 1 ),
	[numberOfDeliveries] integer  NULL ,
	[profit]             decimal(10,3)  NULL 
)
go

CREATE TABLE [District]
( 
	[idDistrict]         integer  IDENTITY  NOT NULL ,
	[xCord]              integer  NOT NULL ,
	[yCord]              integer  NOT NULL ,
	[idCity]             integer  NOT NULL ,
	[name]               varchar(100)  NOT NULL 
)
go

CREATE TABLE [Drive]
( 
	[driveId]            integer  IDENTITY  NOT NULL ,
	[totalFuelConsumption] decimal(10,3)  NULL ,
	[courierUserName]    varchar(100)  NOT NULL ,
	[currentStatus]      integer  NOT NULL 
	CONSTRAINT [CurrentValues_574705964]
		CHECK  ( currentStatus BETWEEN 0 AND 1 )
)
go

CREATE TABLE [Package]
( 
	[packageId]          integer  IDENTITY  NOT NULL ,
	[districtFrom]       integer  NOT NULL ,
	[districtTo]         integer  NOT NULL ,
	[userName]           varchar(100)  NOT NULL ,
	[packageType]        integer  NOT NULL 
	CONSTRAINT [FuelTypeValues_759029626]
		CHECK  ( packageType BETWEEN 0 AND 2 ),
	[weight]             decimal(10,3)  NOT NULL ,
	[driveId]            integer  NULL ,
	[acceptedTime]       datetime  NULL ,
	[deliveryStatus]     integer  NULL 
	CONSTRAINT [DeliveryStatusValues_112718961]
		CHECK  ( deliveryStatus BETWEEN 0 AND 3 )
)
go

CREATE TABLE [TransportOffer]
( 
	[offerId]            integer  IDENTITY  NOT NULL ,
	[courierUserName]    varchar(100)  NOT NULL ,
	[packageId]          integer  NOT NULL ,
	[pricePercentage]    decimal(10,3)  NOT NULL ,
	[accepted]           integer  NULL 
	CONSTRAINT [AcceptedTypeValues_269667141]
		CHECK  ( accepted BETWEEN 0 AND 1 )
)
go

CREATE TABLE [User]
( 
	[userName]           varchar(100)  NOT NULL ,
	[firstName]          varchar(100)  NOT NULL ,
	[lastName]           varchar(100)  NOT NULL ,
	[password]           varchar(100)  NOT NULL ,
	[numberOfSentPackages] integer  NULL 
)
go

CREATE TABLE [Vehicle]
( 
	[licencePlateNumber] varchar(100)  NOT NULL ,
	[fuelType]           integer  NOT NULL 
	CONSTRAINT [FuelTypeValues_370771093]
		CHECK  ( fuelType BETWEEN 0 AND 2 ),
	[fuelConsumption]    decimal(10,3)  NOT NULL 
)
go

ALTER TABLE [Administrator]
	ADD CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([adminName] ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XPKCity] PRIMARY KEY  CLUSTERED ([idCity] ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XAK1City] UNIQUE ([name]  ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XAK2City] UNIQUE ([postalCode]  ASC)
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [XPKCourier] PRIMARY KEY  CLUSTERED ([courierUserName] ASC)
go

ALTER TABLE [District]
	ADD CONSTRAINT [XPKDistrict] PRIMARY KEY  CLUSTERED ([idDistrict] ASC)
go

ALTER TABLE [District]
	ADD CONSTRAINT [XAK1District] UNIQUE ([name]  ASC)
go

ALTER TABLE [Drive]
	ADD CONSTRAINT [XPKDrive] PRIMARY KEY  CLUSTERED ([driveId] ASC)
go

ALTER TABLE [Package]
	ADD CONSTRAINT [XPKPackage] PRIMARY KEY  CLUSTERED ([packageId] ASC)
go

ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [XPKTransportOffer] PRIMARY KEY  CLUSTERED ([offerId] ASC)
go

ALTER TABLE [User]
	ADD CONSTRAINT [XPKUser] PRIMARY KEY  CLUSTERED ([userName] ASC)
go

ALTER TABLE [Vehicle]
	ADD CONSTRAINT [XPKVehicle] PRIMARY KEY  CLUSTERED ([licencePlateNumber] ASC)
go


ALTER TABLE [Administrator]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([adminName]) REFERENCES [User]([userName])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Courier]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([courierUserName]) REFERENCES [User]([userName])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([licencePlateNumber]) REFERENCES [Vehicle]([licencePlateNumber])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [District]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([idCity]) REFERENCES [City]([idCity])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Drive]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([courierUserName]) REFERENCES [Courier]([courierUserName])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Package]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([districtFrom]) REFERENCES [District]([idDistrict])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([districtTo]) REFERENCES [District]([idDistrict])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([userName]) REFERENCES [User]([userName])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([driveId]) REFERENCES [Drive]([driveId])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([courierUserName]) REFERENCES [Courier]([courierUserName])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([packageId]) REFERENCES [Package]([packageId])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

CREATE PROCEDURE grantCourierRequest
	@username varchar(100),
	@licencePlateNumber varchar(100)
AS
BEGIN

	SET NOCOUNT ON;
	declare @curr_username varchar(100)
	declare @curr_lpN varchar(100)
	declare @found int

	set @found=0

	declare @cursor cursor

	set @cursor=cursor for
	select courierUserName, licencePlateNumber
	from dbo.Courier

	open @cursor

	fetch next from @cursor
	into @curr_username, @curr_lpN

	while @@FETCH_STATUS=0
	begin
		if @curr_username=@username
		begin
			set @found=1
			break
		end

		if @curr_lpN=@licencePlateNumber
		begin
			set @found=1
			break
		end
		

		fetch next from @cursor
		into @curr_username, @curr_lpN

	end

	close @cursor
	deallocate @cursor

	if @found=0
	begin
		insert into dbo.Courier (courierUserName,licencePlateNumber)
		values (@username, @licencePlateNumber)
	end
END
GO

CREATE TRIGGER TR_TransportOffer
   ON  TransportOffer 
   AFTER UPDATE
AS 
BEGIN
	declare @pId int
	declare @courierId varchar(100)
	declare @otherCourierId varchar(100)
	declare @otherPId int
	
	select @pId=packageId, @courierId=courierUserName
	from inserted

	declare @cursor cursor

	set @cursor=cursor for
	select courierUserName,packageId
	from dbo.TransportOffer

	open @cursor

	fetch next from @cursor
	into @otherCourierId,@otherPId

	while @@FETCH_STATUS=0
	begin
		if @otherPId=@pId and @otherCourierId<>@courierId
		begin
			delete from dbo.TransportOffer
			where packageId=@otherPId and courierUserName=@otherCourierId
		end

		fetch next from @cursor
		into @otherCourierId,@otherPId
	end

	close @cursor
	deallocate @cursor



END
GO
