'use strict';

var seriesColors = ['#277db6','#f0ad4e','#3c763d','#f1ca3a','#e2431e','#5cb85c','#F7464A','#1c91c0','#43459d','#F7464A','#46BFBD','#B276B2','#6f9654','#e7711b','#f1ca3a'];
var fulldateFormat = "DD/MM/YYYY HH:mm:ss";
var dateFormat = "DD/MM/YYYY";
var timeFormat = "HH:mm";

var DEFAULT_INTERVAL = 5000;
var TIMEOUT = 30000;
var DELAY = 3000;
var DEFAULT_VIEW = null;
var BYPASS = "bypass";
var PREVIEW = "preview";
var UNAUTH = "Unauthorized";

var STATE_INIT = "ini";
var PREVIEW_MODES = [
    {id:'preview',name:'Preview',icon:'fa fa-list-alt',check:true},
    {id:'desktop',name:'Desktop',icon:'fa fa-desktop',check:false, platform:true},
    {id:'mobile',name:'Browser Mobile',icon:'glyphicon glyphicon-globe',check:true, platform:true},
    {id:'nativo',name:'App Mobile',icon:'fa fa-mobile',check:true, platform:true},
    {id:'android',name:'Android',icon:'fa fa-android',check:true, system:true},
    {id:'ios',name:'IOS',icon:'fa fa-apple',check:true, system:true},
    {id:'winphone',name:'WinPhone',icon:'fa fa-windows',check:true, system:true}
];

var linechart = {
    xAxis: {
        categories: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun','Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez']
    },
    yAxis: {
        title: "",
        min: 0,
    },
    options: {
        colors: seriesColors,
        chart: {
            type: 'line'
        }
    },
    series: [{
        name: 'Rio de Janeiro',
        data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
    }, {
        name: 'São Paulo',
        data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
    }, {
        name: 'Brazilia',
        data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
    }, {
        name: 'Curitiba',
        data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
    }],
    title: {
        text: '',
        floating: true

    }
};

var columnchart = {
    xAxis: {
        categories: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun','Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
        crosshair: true
    },
    yAxis: {
        min: 0,
        title: {
            text: 'PIB (%)'
        }
    },
    options: {
        colors: seriesColors,
        chart: {
            type: 'column'
        }
    },
    series: [{
            name: 'Rio de Janeiro',
        data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
        }, {
            name: 'São Paulo',
            data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3]
        }, {
            name: 'Brasília',
                data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2]
        }, {
            name: 'Curitiba',
                data: [42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1]
    }],
    title: {
        text: '',
        floating: false

    }
};


var piechart = {
    options: {
        colors: seriesColors,
        chart: {
            type: 'pie'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
    },
    series: [{
        name: 'Brands',
        colorByPoint: true,
        data: [{
            name: 'Chrome',
            y: 56.33
        }, {
            name: 'Firefox',
            y: 24.03,
            sliced: true,
            selected: true
        }, {
            name: 'Microsoft Internet Explorer',
            y: 10.38
        }, {
            name: 'Safari',
            y: 4.77
        }, {
            name: 'Opera',
            y: 0.91
        }, {
            name: 'Outros',
            y: 0.2
        }]
    }],
    title: {
        text: '',
        floating: false

    }
};

