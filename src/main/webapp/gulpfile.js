/**
 * 
 * @type Module gulp|Module gulp
 * gulp file that will monitor files in the src tree and then move them
 * to the tomcat instance to effect a poor-man's JRebel
 * 
 * Your tomcat server needs to be running and to see the changes
 * This script will also refresh one page of your choice specified in the
 * refresh page. Refreshing the page requires installation of the chrome 
 * live reload plugin 
 * 
 * this file must be in src/main/resources/com/ni/tools/webapp/assets it 
 * assumes it is only working with css, js in the assets folder and below
 * 
 * usage: gulp watch-assets
 * 
 * modules below must be installed globally
 * 
 * the live reload plugin page may have to be the only page up in chrome?
 * 
 */
var gulp = require('gulp');
var tap = require('gulp-tap');
var watch = require('gulp-watch');
var livereload = require('gulp-livereload');

/**
 * this is the list of files to watch will have to change this each usage
 * @type Array
 */
var watchItems = [
    'pages/resources/js/main.js'];

/**
 * the page that you want livereload to refresh
 *  
 */
var pageURL = 'http://localhost:8080/websocket/pages/index.html';

/* html items are handled separately */
var htmlItem = 'pages/index.html';

/* system needs to have TOMCAT_HOME properly defined */
var tomcatHome = process.env.TOMCAT_HOME;



gulp.task('watch-assets', function () {
    livereload.listen();
    watch(watchItems, function (events, done) {

        gulp.start(['copy-assets', 'refresh']);
    });

    watch(htmlItem, function (events, done) {

        gulp.start(['copy-html','reload-html']);
    });
});



gulp.task('copy-assets', function () {
    return gulp.src(watchItems, {'base': './'})

            .pipe(tap(function (file, t)
            {
                console.log('updating ' + file.relative);

            }))
            .pipe(gulp.dest(tomcatHome + '/webapps/websocket'));


});
gulp.task('reload-html', function()
{
    //apparently needs to be done twice
 
             function reloadHTML()
             {
                // livereload.reload(pageURL);
                 livereload.reload(pageURL);
             }
             setTimeout( reloadHTML,200);
            
});


gulp.task('copy-html', function () {
    return gulp.src(htmlItem, {'base': './'})

            .pipe(tap(function (file, t)
            {
                console.log('updating html ' + file.relative);

            }))
            .pipe(gulp.dest(tomcatHome + '/webapps/websocket'))
             
             


});

 

/**
 * the location of the page to refresh will have to be changed as well
 */
gulp.task('refresh', function () {
    console.log('refresh')
    livereload.reload(pageURL);

});