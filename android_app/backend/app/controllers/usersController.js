var express = require('express');
var router = express.Router();
var User = require('../models/user');
var jwt = require('jsonwebtoken');
var bcrypt = require('bcrypt');
var app = express();


router.use(function (req, res, next) {

    // check header or url parameters or post parameters for token
    var token = req.body.token || req.param('token') || req.headers['x-access-token'];
    // decode token
    if (token) {

        // verifies secret and checks exp
        jwt.verify(token, "secretmessage", function (err, decoded) {
            if (err) {
                return res.json({success: false, message: 'Failed to authenticate token.'});
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
    console.log(req.body);
    User.findOne({
        _id: req.body.id
    }, function (err, user) {
        if (err) {
            res.json({'Success': 'Post Failed Something is wrong. Log in first!!'});
        } else if (!user) {
            res.json({'Success': 'User is not Allowed'});
        } else if (user) {
            if (user.admin) {
                next();
            } else {
                res.json({'Success': 'Access Denied!!'});
            }
        }

    });

});

router.post('/admin/add_user', (req, res) => {
    var hashpassword = bcrypt.hashSync(req.body.password, 10);
    var user = new User();
    user.username = req.body.u_username;
    user.email = req.body.email;
    user.phone = req.body.phone;
    user.password = hashpassword;
    user.passwordConf = hashpassword;
    user.user_status = req.body.user_status;
    if(req.body.user_type=="Admin"){
        user.admin = true;
    }else{
        user.admin = false;
    }

    user.save(function (err, Person) {
        if (err) {
            console.log('Error during record insertion : ' + err);
        } else {
            res.json({'Success': 'User successfully inserted!!!'});
        }
    });


});

router.post('/admin/list_user', function (req, res, next) {
    User.find((err, docs) => {
        if (!err) {
            res.render("userList", {      // Displaying view of list.hbs file containing HTML File
                users: docs,                 // Sending all user to view
                layout:""
            });
        }
        else {
            res.json({'Success': 'Failed to Retreive!!!'});
        }
    }).sort({'_id': -1});
});

router.post('/admin/delete_user', (req, res) => {
    User.findByIdAndRemove(req.body._id, (err, doc) => {
        if (!err) {
            res.json({'Success': 'User added Successfully!!'});
        }
        else {
            res.json({'Success': 'Error in Appointment delete :' + err});
        }
    });
});

router.post('/admin/edit_user', (req, res) => {
    User.findById(req.body._id, (err, doc) => {
        if (!err) {
            res.json(doc);
        }
    });
});

router.post('/admin/update_user', (req, res) => {
    var all_data = req.body;
    all_data.password = bcrypt.hashSync(all_data.password, 10);
    User.findOneAndUpdate({_id: req.body._id}, all_data, {new: true}, (err, doc) => { //find by id and update it
        if (!err) {
            res.json({'Success': 'User Edited Successfully!!'});
        }
        else {
            res.json({'Success': 'Error during record update : ' + err});
        }
    });
});



module.exports = router;
