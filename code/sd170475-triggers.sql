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