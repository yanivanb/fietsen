insert into cursussen(naam) values('testGroep');
insert into groepscursussen(id, van, tot)
values((select id from cursussen where naam='testGroep'),'2018-01-01','2018-01-01');
insert into cursussen(naam) values('testIndividueel');
insert into individuelecursussen(id, duurtijd)
values((select id from cursussen where naam = 'testIndividueel'), 3);