'use strict';

const express = require('express');
var http = require('http');
const request = require('request-promise-native')

// Constants
const PORT = 8081;
const HOST = '0.0.0.0';

// App
const app = express();
app.get('/forecast', async (req, res) => {
    const uri = 'http://api.weatherapi.com/v1/forecast.json?key=1baa4a47b6694dd89d274000212911&q=London&days=1&aqi=no&alerts=no'

    const forecast = await request(uri)
    res.json(forecast)
});

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);