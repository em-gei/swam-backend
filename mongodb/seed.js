// Accesso al database specificato via env (valido solo per il primo avvio)
db = db.getSiblingDB('bikeworld');

db.createUser({
  user: "app_user",
  pwd: "4pp_p4ssw0rd",
  roles: [{
    role: "readWrite",
    db: "bikeworld"
  }]
});

db.user.insertMany([
  {
    "firstname": "Ted",
    "lastname": "Mosby",
    "email": "ted@mosby.com",
    "post_count": 0,
    "active": true
  }
]);

print("Database setup and initialization completed");
