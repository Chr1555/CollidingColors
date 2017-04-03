var colors = require('colors');
var express = require('express');
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var path = require('path');

var players = [];
var number = 0;
var turn = 0;
var port = 3000;
var menunggu = true;


app.use(express.static(path.join(__dirname, 'public')));

// standard get http method
app.get('/', function(req, res){
    res.sendFile(path.join(__dirname, 'views/index.html'));
});

// SOCKET PROGRAMMING
io.on('connection', function(socket){
	if (menunggu) {
		console.log('Client connected'.green);
	   console.log(number);

	   var player = {
	   	soket:socket,
	   	playerke:number,
	   	point:0
	   };
	   players.push(player);
	   
	   // create client
	   socket.on('create', function(){
	   		console.log('Client ' + number +' created');
	   		socket.emit('create',number);
	   		number = number + 1;
	   });
	}else{
		socket.emit('kick');
	}


   // disconnect client
   socket.on('disconnect', function(){
   	
      console.log('Client Disconnected'.red); 
      var max = 0;
      var urutan = 0;
      players.forEach(klien =>{
      		if (klien.point>max) {
      			max = klien.point;
      			urutan = klien.playerke;
      		}
      });
      io.emit('end', urutan, max);
      players.forEach(klien =>{
      		players.pop();
      });
      number = 0;
      turn = 0;
      menunggu = true;
	  
   });
   
   // set number of player, player turn, & choose
   socket.on('choose', function(num, pilih){
		var playnum;
		if (players.length < 4){
			playnum = players.length;
		}else {
			playnum = 4;
		}

		if(num != (turn%playnum)){
			socket.emit('choose', 'This is Not Your tTurn !!!');
		}else{
			socket.emit('choose', 'Wait for Another Player...');
			var poin;
			menunggu = false;
			players.forEach(klien => {
				if(klien.playerke === num){
					poin = klien.point;
				}
			});
			socket.emit('checkpoint', poin, pilih, num)
			io.emit('choosen', pilih, turn%playnum, playnum);
			turn = turn + 1;
		}
   });
   
   // chatting
   socket.on('chat', function(num, msg){
      console.log('Chat from player-' + num + ': ' + msg);
      io.emit('chat', num, msg);
   });

   // set point from checkpoint
   socket.on('setpointplus', function(num, tambah){
   		console.log(num + ' ' + tambah);
   		players.forEach(klien =>{
			if(klien.playerke === num){
   				klien.point = klien.point + tambah;
   				io.emit('setscore', klien.point, num);
	   		}
	   	});
   });

   
});

http.listen(port, function(){
    console.log("Server is listening on localhost: " + port);
});