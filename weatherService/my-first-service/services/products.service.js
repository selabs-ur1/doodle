"use strict";

const DbService = require("moleculer-db");

module.exports = {
	name: "products",
	mixins: [DbService]
};