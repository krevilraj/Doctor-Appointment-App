// =================================================================
// get the packages we need ========================================
// =================================================================
var express 	= require('express');
var app         = express();
var bodyParser  = require('body-parser');
var morgan      = require('morgan');
var mongoose    = require('mongoose');
const path = require('path');

var jwt    = require('jsonwebtoken'); // used to create, sign, and verify tokens
var config = require('./config'); // get our config file
var User   = require('./app/models/user'); // get our mongoose model
var cors   = require('cors');
const exphbs = require('express-handlebars');

// =================================================================
// Set Engine for view =============================================
// =================================================================
app.set('views', path.join(__dirname, '/app/views/'));

app.engine('hbs', exphbs({
    extname: 'hbs',
    defaultLayout: 'mainLayout',
    layoutsDir: __dirname + '/views/layouts/',
    partialsDir: __dirname + '/views/partials/',
    helpers: {
        ifEqual: function (v1, v2, options) {
            if (v1 === v2) {
                return options.fn(this);
            }
            return options.inverse(this);
        },
        ifNotEqual: function (v1, v2, options) {
            if (v1 != v2) {
                return options.fn(this);
            }
            return options.inverse(this);
        },
        increment: function (v1) {
            return v1 + 1;
        },
        trimString: function (passedString) {
            var StrippedString = passedString.replace(/(<([^>]+)>)/ig,"");
            var theString = StrippedString.substring(0, 150);
            return theString;
        },
		trimTitle: function (passedString) {
            var StrippedString = passedString.replace(/(<([^>]+)>)/ig,"");
            var theString = StrippedString.substring(0, 35);
            return theString;
        }
    }
}));
app.set('view engine', 'hbs');


// =================================================================
// configuration ===================================================
// =================================================================

var port = process.env.PORT || 3000; // used to create, sign, and verify tokens
// connect to database
mongoose.connect(config.database, { useNewUrlParser: true }, (err) => {
    if (!err) { console.log('MongoDB Connection Succeeded.') }
    else { console.log('Error in DB connection : ' + err) }
});

// use body parser so we can get info from POST and/or URL parameters
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// use morgan to log requests to the console
app.use(morgan('dev'));

// =================================================================
// Controllers =====================================================
// =================================================================
const loginController = require('./app/controllers/loginController');
const frontendController = require('./app/controllers/frontendController');
const appointmentController = require('./app/controllers/appointmentController');
const forumController = require('./app/controllers/forumController');
const feedbackController = require('./app/controllers/feedbackController');
const userController = require('./app/controllers/usersController');


// =================================================================
// routes ==========================================================
// =================================================================
app.get('/setup', function(req, res) {

	// create a sample user
	var nick = new User({
		name: 'Nick Cerminara',
		password: 'password',
		admin: true
	});
	nick.save(function(err) {
		if (err) throw err;

		console.log('User saved successfully');
		res.json({ success: true });
	});
});

// basic route (http://localhost:8080)
app.get('/', function(req, res) {
	res.send('Hello! The API is at http://localhost:' + port + '/api');
});

// ---------------------------------------------------------
// get an instance of the router for api routes
// ---------------------------------------------------------
var apiRoutes = express.Router();





// ---------------------------------------------------------
// authenticated routes
// ---------------------------------------------------------

var corsOptions ={
    "origin": "*",
    "methods": "GET,HEAD,PUT,PATCH,POST,DELETE",
    "preflightContinue": false,
    "optionsSuccessStatus": 204
};
app.use(express.static(__dirname + '/images'));

app.use('/api',cors(corsOptions), frontendController);
app.use('/api',cors(corsOptions), loginController);
app.use('/api',cors(corsOptions), appointmentController);
app.use('/api',cors(corsOptions), forumController);
app.use('/api',cors(corsOptions), feedbackController);
app.use('/api',cors(corsOptions), userController);

// =================================================================
// start the server ================================================
// =================================================================
app.listen(port);
console.log('Server runs at http://localhost:' + port);
