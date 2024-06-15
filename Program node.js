const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql');

const app = express();
const PORT = 8000;

// Database connection
const con = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "", // Add your MySQL password here
    database: "catchtheobject"
});

// Connect to the database
con.connect(function(err) {
    if (err) {
        console.error("Error connecting to MySQL database:", err.message);
        return;
    }
    console.log("Connected to MySQL database!");
});

// Middleware to parse JSON bodies
app.use(bodyParser.json());

// Handle signup POST request
app.post('/signup', function(req, res) {
    const { username, password } = req.body;

    if (!username || !password) {
        return res.status(400).send("Username and password are required");
    }

    const userData = {
        username: username,
        password: password
    };

    con.query('INSERT INTO users SET ?', userData, function(err, result) {
        if (err) {
            console.error("Error inserting user into database:", err.message);
            return res.status(500).send("Error signing up: " + err.message);
        }
        console.log("User signed up successfully with ID:", result.insertId);
        res.status(200).send("User signed up successfully");
    });
});

// Login route
app.post('/login', function(req, res) {
    const { username, password } = req.body;

    if (!username || !password) {
        return res.status(400).send("Username and password are required");
    }

    con.query('SELECT * FROM users WHERE username = ?', [username], function(err, result) {
        if (err) {
            console.error("Error querying database:", err.message);
            return res.status(500).send("Error logging in: " + err.message);
        }

        if (result.length === 0) {
            return res.status(401).send("Invalid username or password");
        }

        const user = result[0];
        if (user.password !== password) {
            return res.status(401).send("Invalid username or password");
        }

        res.status(200).send("Login successful");
    });
});

// Store highest score route
app.post('/score', function(req, res) {
    const { username, score } = req.body;

    if (!username || score === undefined || isNaN(score)) {
        return res.status(400).send("Username and score (as a number) are required");
    }

    // Check if the user already has a highest score
    con.query('SELECT score FROM score WHERE username = ?', [username], function(err, result) {
        if (err) {
            console.error("Error querying database:", err.message);
            return res.status(500).send("Error checking highest score: " + err.message);
        }

        // If the user has a previous highest score, update it if the new score is higher
        if (result.length > 0) {
            const previousHighestScore = result[0].score;
            if (score > previousHighestScore) {
                con.query('UPDATE score SET score = ? WHERE username = ?', [score, username], function(err) {
                    if (err) {
                        console.error("Error updating score:", err.message);
                        return res.status(500).send("Error updating score: " + err.message);
                    }
                    console.log(`Updated highest score for ${username} to ${score}`);
                    res.status(200).send("Score updated successfully");
                });
            } else {
                res.status(200).send("Score not higher than previous highest score");
            }
        } else {
            // If the user doesn't have a previous highest score, insert the new score
            con.query('INSERT INTO score (username, score) VALUES (?, ?)', [username, score], function(err) {
                if (err) {
                    console.error("Error inserting score:", err.message);
                    return res.status(500).send("Error inserting score: " + err.message);
                }
                console.log(`Inserted new highest score for ${username}: ${score}`);
                res.status(200).send("Score inserted successfully");
            });
        }
    });
});

// Start the server
app.listen(PORT, function() {
    console.log(`Server is running on port ${PORT}`);
});
