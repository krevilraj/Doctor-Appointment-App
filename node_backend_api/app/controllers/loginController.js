var express = require('express');
var router = express.Router();
var User = require('../models/user');
var jwt = require('jsonwebtoken');
var bcrypt = require('bcrypt');
var app = express();

router.post('/profile', (req, res) => {
    User.findOne({
        _id: req.body._id
    }, function (err, user) {
        if (err) {
            res.json({'Success': 'Post Failed Something is wrong. Log in first!!1'});
        } else if (!user) {
            res.json({'Success': 'Post Failed Something is wrong. Log in first!!2'});
        } else if (user) {

            if (user.username == req.body.username) {
                res.json(user);
            } else {
                res.json({'Success': 'Authentication Failed!!'});
            }
        }

    });

});

router.post('/authenticate', function (req, res) {
    var response = res;
    console.log(req.body);
    // find the user
    User.findOne({
        email: req.body.email
    }, function (err, user) {

        if (err) throw err;

        if (!user) {
            res.json({Success: 'false', message: 'Authentication failed. User not found.'});
        } else if (user) {

            if (user.user_status == "Banned") {
                res.send({Success: 'false', message: "You are Banned by Admin"}); // send response to ajax call to view
            }
            else {

                bcrypt.compare(req.body.password, user.password, function (err, res) {
                    if (res) {
                        // Passwords match

                        // if user is found and password is right
                        // create a token
                        var payload = {
                            admin: user.admin
                        }
                        var token = jwt.sign(payload, "secretmessage", {
                            expiresIn: 86400 // expires in 24 hours
                        });

                        response.json({
                            Success: 'Success!',
                            message: 'Welcome ' + user.username,
                            token: token,
                            username: user.username,
                            _id: user._id,
                            admin: user.admin
                        });
                    } else {
                        // Passwords don't match
                        response.json({Success: 'false', message: 'Authentication failed. Wrong password.'});
                    }
                });
            }

        }

    });
});
router.post('/signup', function (req, res, next) {
    console.log(req.body);
    var personInfo = req.body;

    // Validate if the user enter email, username, password and confirm password
    if (!personInfo.email || !personInfo.username || !personInfo.password || !personInfo.passwordConf) {
        res.send({"Success": "Fill all the input fields"});
    } else {
        // validate if the password and confirm password is same or not
        if (personInfo.password == personInfo.passwordConf) {

            // find the email if the email in table
            User.findOne({email: personInfo.email}, function (err, data) {

                // if the email is not already taken
                if (!data) {
                    var c;
                    // find the last user and take unique_id from that to variable c for new user
                    User.findOne({}, function (err, data) {

                        var hashpassword = bcrypt.hashSync(personInfo.password, 10);
                        //Initialize the user Model object with variable or value from the post form
                        var newPerson = new User({
                            email: personInfo.email,
                            username: personInfo.username,
							image: personInfo.image,
                            password: hashpassword,
                            passwordConf: hashpassword,
                            admin: false
                        });

                        // Save it to table User
                        newPerson.save(function (err, Person) {
                            if (err)
                                console.log(err);
                            else
                                console.log('Success');
                        });

                    }).sort({_id: -1}).limit(1);
                    res.send({"Success": "You are regestered,You can login now."}); // send response to ajax call to view
                } else {
                    res.send({"Success": "Email is already used."}); // send response to ajax call to view
                }

            });
        } else {
            res.send({"Success": "password is not matched"}); // send response to ajax call to view
        }
    }
});
// ---------------------------------------------------------
// route middleware to authenticate and check token
// ---------------------------------------------------------


router.use(function (req, res, next) {

    // check header or url parameters or post parameters for token
    var token = req.body.token || req.param('token') || req.headers['x-access-token'];

    // decode token
    if (token) {

        // verifies secret and checks exp
        jwt.verify(token, "secretmessage", function (err, decoded) {
            if (err) {
                return res.json({success: false, message: 'Failed to authenticate token.'});
            } else {
                // if everything is good, save to request for use in other routes
                req.decoded = decoded;
                next();
            }
        });

    } else {

        // if there is no token
        // return an error
        return res.status(403).send({
            success: false,
            message: 'No token provided.'
        });

    }

});

router.post('/edtProfile', (req, res) => {
	var all_data = req.body;
    all_data.password = bcrypt.hashSync(all_data.password, 10);
	all_data.passwordConf = all_data.password;
    User.findOneAndUpdate({_id: req.body._id}, all_data, {new: true}, (err, doc) => {
        if (!err) {
            res.json({'Success': 'Profile Updated Successfully!!', 'username': doc.username});
        }
        else {
            console.log('Error during record update : ' + err);
        }
    });
});

router.post('/forumPost', (req, res) => {
    User.findOne({
        _id: req.body._id
    }, function (err, user) {
        if (err) {
            res.json({'Success': 'Post Failed Something is wrong. Log in first!!'});
        } else if (!user) {
            res.json({'Success': 'Post Failed Something is wrong. Log in first!!'});
        } else if (user) {

            console.log(user.username);
            console.log(req.body.username);
            if (user.username == req.body.username) {
                var forum = new Forum();

                forum.title = req.body.title;
                forum.description = req.body.description;
                forum.author = req.body.username;
                forum.save((err, doc) => {
                    if (err) {
                        console.log('Error during record insertion : ' + err);
                    } else {
                        res.json({'Success': 'Post successfully Placed!!!'});
                    }
                });
            } else {
                res.json({'Success': 'Authentication Failed!!'});
            }
        }

    });

});

router.post('/post_comment', (req, res) => {
    User.findOne({
        _id: req.body._id
    }, function (err, user) {
        if (err) {
            res.json({'Success': 'Post Failed Something is wrong. Log in first!!1'});
        } else if (!user) {
            res.json({'Success': 'Post Failed Something is wrong. Log in first!!2'});
        } else if (user) {


            if (user.username == req.body.username) {
                var forumComment = new ForumComment();

                forumComment.forum_id = req.body.forum_id;
                forumComment.description = req.body.description;
                forumComment.author = req.body.username;


                forumComment.save((err, doc) => {
                    if (err) {
                        console.log('Error during record insertion : ' + err);
                    } else {
                        res.json({'Success': 'Your comment successfully posted'});
                    }
                });
            } else {
                res.json({'Success': 'Authentication Failed!!'});
            }
        }

    });

});


router.get('/users', function (req, res) {
    User.find({}, function (err, users) {
        res.json(users);
    });
});

/*router.post('/login', function (req, res, next) {
	//console.log(req.body);
	User.findOne({email:req.body.email},function(err,data){
		if(data){
		    console.log(data.user_type);
			if(data.user_status=="Banned"){
                res.send({"Success":"You are Banned by Admin"}); // send response to ajax call to view
			}
			else if(data.password==req.body.password){
				//console.log("Done Login");
				req.session.userId = data.unique_id;        // if user found in table then save it to session
				req.session.username = data.username;       // in every page session can be accessed to check
				req.session.user_type = data.user_type;     // user is authenticate or not
				console.log(req.session.username);

				res.send({"Success":"Success!"});       // send response to ajax call to view

			}else{
				res.send({"Success":"Wrong password!"}); // send response to ajax call to view
			}
		}else{
			res.send({"Success":"This Email Is not regestered!"}); // send response to ajax call to view
		}
	});
});*/


module.exports = router;
