"use strict";


const DbService = require("moleculer-db");
var http = require('http');
const bodyParser = require('body-parser');
const axios = require('axios');

module.exports = {
	name: "weather",
	actions : {
        async get(){
            //let promise = await Promise.resolve({ 
            //    answer: http.get("http://api.weatherapi.com/v1/forecast.json?key=1baa4a47b6694dd89d274000212911&q=London&days=1&aqi=no&alerts=no") 
            //});
            let promise = axios.get("http://api.weatherapi.com/v1/forecast.json?key=1baa4a47b6694dd89d274000212911&q=Paris&days=7&aqi=no&alerts=no")
                .then(response => {
                    console.log(response);
                    return Object.assign({}, response.data);
            });
            let dataCool= 
            promise.then(data => {
                console.log('Request successful:', data);
                return data;
              }, err => {
                console.log('Request failed:', err);
                return err;
              });

            return dataCool;


        }
    }
};