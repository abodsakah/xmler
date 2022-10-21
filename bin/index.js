#! /usr/bin/env node
const yargs = require("yargs");
const { interrupter } = require("../src/formater.js");

const usage = "\nUsage: xmler -i [Input File] -o [Output File] [Options]";
const options = yargs
	.usage(usage)
	.option("i", {
		alias: "input",
		describe: "Input file",
		type: "string",
		demandOption: true
	})
	.option("o", {
		alias: "output",
		describe: "Output file",
		type: "string",
		demandOption: true
	})
	.help(true).argv;

function main() {
	console.log(interrupter(options.input));
}

main();
