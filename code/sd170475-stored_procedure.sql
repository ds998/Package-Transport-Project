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