var log = Java.type("io.vertx.core.logging.LoggerFactory").getLogger('javascriptVerticle.js');

exports.vertxStart = function() {
    log.info("javascriptVerticle is starting...");

    vertx.setPeriodic(1000, function (id) {
        vertx.eventBus().send("silverware.topic", "Hello from javascriptVerticle.js");
    });
};

exports.vertxStop = function() {
    log.info('javascriptVerticle stop called');
};