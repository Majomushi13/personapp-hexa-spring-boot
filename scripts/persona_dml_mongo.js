use persona_db

db.persona.insertMany([
	{
		"_id": NumberInt(123456789),
		"nombre": "Pepe",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(987654321),
		"nombre": "Pepito",
		"apellido": "Perez",
		"genero": "M",
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(321654987),
		"nombre": "Pepa",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(30),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(147258369),
		"nombre": "Pepita",
		"apellido": "Juarez",
		"genero": "F",
		"edad": NumberInt(10),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	},
	{
		"_id": NumberInt(963852741),
		"nombre": "Fede",
		"apellido": "Perez",
		"genero": "M",
		"edad": NumberInt(18),
		"_class": "co.edu.javeriana.as.personapp.mongo.document.PersonaDocument"
	}
], { ordered: false })


db.telefono.insertMany([
    {
        "_id": NumberInt(1),
        "num": "123456789",
        "oper": "Movistar",
        "dueno": NumberInt(123456789),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    },
    {
        "_id": NumberInt(2),
        "num": "987654321",
        "oper": "Claro",
        "dueno": NumberInt(987654321),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    },
    {
        "_id": NumberInt(3),
        "num": "321654987",
        "oper": "Tigo",
        "dueno": NumberInt(321654987),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    },
    {
        "_id": NumberInt(4),
        "num": "147258369",
        "oper": "Movistar",
        "dueno": NumberInt(147258369),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    },
    {
        "_id": NumberInt(5),
        "num": "963852741",
        "oper": "Claro",
        "dueno": NumberInt(963852741),
        "_class": "co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument"
    }
], { ordered: false });

db.estudios.insertMany([
    {
        "_id": NumberInt(1),
        "id_prof": NumberInt(1),
        "cc_per": NumberInt(123456789),
        "fecha": ISODate("2010-06-15"),
        "univer": "Universidad Nacional",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    },
    {
        "_id": NumberInt(2),
        "id_prof": NumberInt(2),
        "cc_per": NumberInt(987654321),
        "fecha": ISODate("2012-07-20"),
        "univer": "Universidad de los Andes",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    },
    {
        "_id": NumberInt(3),
        "id_prof": NumberInt(3),
        "cc_per": NumberInt(321654987),
        "fecha": ISODate("2015-09-10"),
        "univer": "Pontificia Universidad Javeriana",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument"
    }
], { ordered: false });

db.profesion.insertMany([
    {
        "_id": NumberInt(1),
        "nom": "Ingeniero",
        "des": "Ingeniería de software",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
    },
    {
        "_id": NumberInt(2),
        "nom": "Médico",
        "des": "Medicina general",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
    },
    {
        "_id": NumberInt(3),
        "nom": "Arquitecto",
        "des": "Diseño de edificaciones",
        "_class": "co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument"
    }
], { ordered: false });