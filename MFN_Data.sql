use master
go

use MFN_Data
go

create or alter proc procUpdateBoomQB
	@firstn varchar(25), @lastn varchar(25), @postcampacc int,@secondacc int = null, 
	@thirdacc int = null, @fourthacc int = null, @fifthacc int = null
as
begin
	set nocount on;
	update top(1) QBProgress
	set postcampacc = @postcampacc,
		secondacc = isnull(@secondacc, secondacc),
		thirdacc = ISNULL(@thirdacc, thirdacc),
		fourthacc = ISNULL(@fourthacc, fourthacc),
		fifthacc = ISNULL(@fifthacc, fifthacc)
	where firstn = @firstn and lastn = @lastn


end
go

create or alter proc procInsertQBPreCamp 
	@Vol int, @PrecampAcccurrent int, @PrecampAccmax int, @firstName varchar(25), @lastName varchar(25)
as
begin
	set nocount on;
	insert into QB 
	(Vol, PrecampAccCurrent, PrecampAccMax,  FirstName, LastName)
	values
	(@Vol, @PrecampAcccurrent, @PrecampAccmax,  @firstName, @lastName)
end
go

create or alter proc procUpdateQBPostCamp
	@boombust bit, @postcampacccurrent int, @postcampaccmax int, @firstname varchar(25),@lastname varchar(25)
as
begin
	set nocount on;
	update top(1) QB
	set BoomBust = @boombust,
		PostcampAccCurrent = @postcampacccurrent,
		PostcampAccMax = @postcampaccmax
	where FirstName = @firstname and LastName = @lastname
end
go

create or alter proc AddBoomQBs
as
begin
set nocount on;

	insert into QBProgress(firstn,lastn,vol,draftacc,postcampacc)
	select q.FirstName, q.LastName, q.Vol, q.PrecampAccMax, q.PostcampAccMax
	from QB as q
	where BoomBust = 1 and not exists(select ID from QBProgress as qp where qp.firstn = q.FirstName and qp.lastn = q.LastName)
end
go

create or alter proc procInsertSpdPreCamp
	@firstname varchar(25),@lastname varchar(25),@tcweight int,@tcspeed int
as
begin 
	set nocount on;
	insert into Speed
	(FirstName,LastName,TCWeight,TCSpeed)
	values
	(@firstname,@lastname,@tcweight,@tcspeed)
end
go

create or alter proc procUpdateSpdEoS
	@firstn varchar(25),@lastn varchar(25),@eosweight int, @eosspd int
as
begin
	set nocount on;
	update top(1) Speed
	set EndOfSeasonWeight = @eosweight,
		EoSSpeed = @eosspd
	where FirstName = @firstn and LastName = @lastn
end
go

create or alter view QBsAccGap
as
select top 100 percent q.FirstName,q.LastName,q.PrecampAccMax,
		 q.PostcampAccMax, (q.PostcampAccMax - q.PostcampAccCurrent) as 'Postcamp Acc Gap',
		(q.PrecampAccMax - q.PrecampAccCurrent) as 'Precamp Acc Gap',q.BoomBust as 'Boom',q.Vol
from QB as q
go

select * from QBsAccGap
order by  PostcampAccMax desc
go
	

