
if (typeof jQuery === "undefined") {
    throw new Error("AdminLTE requires jQuery");
}

$.caixaFixAngular = {
    fix: function ajustarLayoutAngular() {
        var headerHeight = $('.main-header').outerHeight();
        var caixaPageHeight = $('.cx-page').outerHeight();
        var window_height = $(window).height();
        var sidebar_height = $(".main-sidebar").height();
        var footerHeight = $('.main-footer').outerHeight();
        if (caixaPageHeight < window_height) {
            $(".content-wrapper, .right-side").css('min-height', window_height - (footerHeight + headerHeight));
        } else {
            $(".content-wrapper, .right-side").css('min-height', caixaPageHeight);
        }
    }
}