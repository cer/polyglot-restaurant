// Testacular configuration
// Generated on Mon Dec 24 2012 14:08:05 GMT-0800 (PST)


// base path, that will be used to resolve files and exclude
basePath = '..';


// list of files / patterns to load in the browser
files = [
  JASMINE,
  JASMINE_ADAPTER,
  'test/applib/angular/angular.js',
  'test/applib/angular/angular-*.js',
  'test/lib/angular/angular-mocks.js',
  'app/public/js/*.js',
  'test/unit/*.js'  
];


// list of files to exclude
exclude = [
	'app/public/js/bootstrap.min.js',
	'test/*lib/*-min.js'
];


// test results reporter to use
// possible values: 'dots', 'progress', 'junit'
reporters = ['progress'];


// web server port
port = 8080;


// cli runner port
runnerPort = 9100;


// enable / disable colors in the output (reporters and logs)
colors = true;


// level of logging
// possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
logLevel = LOG_INFO;


// enable / disable watching file and executing tests whenever any file changes
autoWatch = true;


// Start these browsers, currently available:
// - Chrome
// - ChromeCanary
// - Firefox
// - Opera
// - Safari (only Mac)
// - PhantomJS
// - IE (only Windows)
browsers = ['Chrome'];


// If browser does not capture in given timeout [ms], kill it
captureTimeout = 5000;


// Continuous Integration mode
// if true, it capture browsers, run tests and exit
singleRun = false;
