"use strict";


const DbService = require("moleculer-db");
var http = require('http');
const bodyParser = require('body-parser');


module.exports = {
	name: "weather",
	actions : {
        async get(){
            let promise = await Promise.resolve({ 
                answer: http.get("http://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=hourly,daily&appid=d3664c8e7e115fa76ecf90c2ae45a82e") 
            });
            console.log(promise)
            return promise
        }
    }
};