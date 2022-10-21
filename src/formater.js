const fs = require("fs");

function interrupter(filePath) {
	const file = fs.readFileSync(filePath, "utf8");
	const lines = file.split("\n");
	const people = { people: [] };
	for (let i = 0; i < lines.length; i++) {
		const line = lines[i].split("|");

		const person = {};

		if (line[0] === "P") {
			person.firstname = line[1];
			person.lastname = line[2];
		}

		if (line[0] === "T") {
			person.phone = {
				mobile: line[1],
				home: line[2]
			};
		}

		if (line[0] === "A") {
			person.street = line[1];
			person.city = line[2];
			person.postalcode = line[3];
		}

		if (line[0] === "F") {
			person.family = {
				name: line[1],
				born: line[2]
			};
		}

		people.people.push({ person });
	}

	return JSON.stringify(people);
}

module.exports = {
	interrupter
};
