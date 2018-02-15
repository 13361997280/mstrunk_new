(function ($) { 
    var methods = {
        init : function (options) { 
            return this.each(function () {
                var 
                    $this = $(this),
                    data = $this.data('monthpicker'),
                    year = (options && options.year) ? options.year : (new Date()).getFullYear(),
                    settings = $.extend({
                        pattern: 'mm-yyyy',
                        startMonth: null,
                        endMonth: null,
                        mode : 'single',
                        startYear: year,
                        endYear : year,
                        sYear: year - 10,
                        finalYear: year,
                        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                        id: "monthpicker_" + (Math.random() * Math.random()).toString().replace('.', ''),
                        openOnFocus: true,
                        clickNum : 0,
                        callback : function(){},
                        //暂不支持
                        disabledMonths: []
                    }, options);

                settings.dateSeparator = settings.pattern.replace(/(mmm|mm|m|yyyy|yy|y)/ig,'');

                // If the plugin hasn't been initialized yet for this element
                if (!data) {

                    $(this).data('monthpicker', {
                        'target': $this,
                        'settings': settings
                    });

                    if (settings.openOnFocus === true) {
                        $this.on('focus', function () {
                            $this.monthpicker('show');
                        });
                    }

                    $this.monthpicker('parseInputValue', settings);
                    
                    if(settings.mode == 'range'){
                    	$this.val(settings.startYear + settings.dateSeparator + settings.startMonth + " - " + 
                    			settings.endYear + settings.dateSeparator + settings.endMonth);
                    }else if(settings.mode == 'single'){
                    	$this.val(settings.startYear + settings.dateSeparator + settings.startMonth);
                    }
                    
                    $this.on("change",function(){
                    	settings.callback($(this).val());
                    });
                    
                    $this.monthpicker('mountWidget', settings);

                    $this.on('monthpicker-click-month', function (e, month, year) {
                        $this.monthpicker('setValue', settings);
                        $this.monthpicker('hide');
                    });

                    // hide widget when user clicks elsewhere on page
                    $this.addClass("mtz-monthpicker-widgetcontainer");
                    $(document).unbind("mousedown.mtzmonthpicker").on("mousedown.mtzmonthpicker", function (e) {
                        if (!e.target.className || e.target.className.toString().indexOf('mtz-monthpicker') < 0) {
                            $(this).monthpicker('hideAll'); 
                        }
                    });
                }
            });
        },

        show: function () {
            $(this).monthpicker('hideAll'); 
            var widget = $('#' + this.data('monthpicker').settings.id);
            widget.css("top", this.offset().top  + this.outerHeight());
            if ($(window).width() > (widget.width() + this.offset().left) ){
                widget.css("left", this.offset().left);
            } else {
                widget.css("left", this.offset().left - widget.width());
            }
            widget.show();
            widget.find('select').focus();
        	this.monthpicker('reSelect');
            this.trigger('monthpicker-show');
        },

        reSelect : function(){
        	var settings = this.data('monthpicker').settings;
        	var widget = $('#' + settings.id);
        	this.monthpicker('parseInputValue', settings);
         	
        	widget.find(".mtz-monthpicker-year").change();
        },
        
        hide: function () { 
        	var settings = this.data('monthpicker').settings;
            var widget = $('#' + settings.id);
            if (widget.is(':visible')) {
            	settings.clickNum = 0;
                widget.hide();
                this.trigger('monthpicker-hide');
            }
        },
        hideAll: function () {
            $(".mtz-monthpicker-widgetcontainer").each(function () {
                if (typeof($(this).data("monthpicker"))!="undefined") { 
                    $(this).monthpicker('hide'); 
                }
            });
        },

        setValue: function (values) {
            var 
                sm = values.startMonth,
                em = values.endMonth,
                sy = values.startYear;
            	ey = values.endYear;
            var settings = $(this).data('monthpicker').settings;
            $.extend(settings,values);
            if(settings.pattern.indexOf('mm') >= 0 && sm < 10 && (sm+"").indexOf("0") != 0) {
            	sm = '0' + sm;
            }
            
            if(settings.pattern.indexOf('mm') >= 0 && em < 10 && (em+"").indexOf("0") != 0) {
            	em = '0' + em;
            }

            if(settings.pattern.indexOf('yyyy') < 0) {
                year = year.toString().substr(2,2);
            } 

            if(sm == em && sy == ey){
            	this.val(sy + settings.dateSeparator + sm); 
            }else{
            	this.val(sy + settings.dateSeparator + sm + " - " + ey + settings.dateSeparator + em); 
            } 
            this.change();
        },

        disableMonths: function (months) {
            var 
                settings = this.data('monthpicker').settings,
                container = $('#' + settings.id);

            settings.disabledMonths = months;

            container.find('.mtz-monthpicker-month').each(function () {
                var m = parseInt($(this).data('month'));
                if ($.inArray(m, months) >= 0) {
                    $(this).addClass('ui-state-disabled');
                } else {
                    $(this).removeClass('ui-state-disabled');
                }
            });
        },
        
        mountWidget: function (settings) {
            var
                monthpicker = this,
                container = $('<div id="'+ settings.id +'" class="ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" />'),
                header = $('<div class="ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-all mtz-monthpicker" />'),
                combo = $('<select class="mtz-monthpicker mtz-monthpicker-year" />'),
                table = $('<table class="mtz-monthpicker" style="margin:0" />'),
                tbody = $('<tbody class="mtz-monthpicker" />'),
                tr = $('<tr class="mtz-monthpicker" />'),
                okBtn = $('<input type="button" value="确定" style="width:250px;" class="mtz-monthpicker-btn btn">'),
                td = '',
                startYear = settings.startYear,
                endYear = settings.endYear,
                option = null,
                attrStartYear = $(this).data('start-year'),
                attrEndYear = $(this).data('end-year'),
                attrSYear = $(this).data('s-year'),
                attrFinalYear = $(this).data('final-year');

            if (attrStartYear) {
                settings.startYear = attrStartYear;
            }

            if (attrSYear) {
                settings.sYear = attrSYear;
            }

            if (attrFinalYear) {
                settings.finalYear = attrFinalYear;
            }

            container.css({
                position:'absolute',
                zIndex:999999,
                whiteSpace:'nowrap',
                width:'250px',
                overflow:'hidden',
                textAlign:'center',
                display:'none',
                top: monthpicker.offset().top + monthpicker.outerHeight(),
                left: monthpicker.offset().left
            }); 
            
            combo.on('change', function () { 
                var months = $(this).parent().parent().find('td[data-month]');
                months.removeClass('ui-state-active');
                var v = $(this).val(),s = 1,e = 0;
                if (v > settings.startYear && v < settings.endYear) {
                	months.addClass('ui-state-active');
                }else if(v == settings.endYear){ 
            		if(settings.endYear == settings.startYear){
            			s = settings.startMonth; e = settings.endMonth;
            		}else{
            			s = 0;e = settings.endMonth;
            		}
                }else if(v == settings.startYear){ 
            		if(settings.endYear == settings.startYear){
            			s = settings.startMonth; e = settings.endMonth;
            		}else{
            			s = settings.startMonth;e = 12;
            		} 
                }
                for(var i = s; i<= e;i++){
                	$('#' + settings.id).find('.mtz-mp-mon-' + i).addClass('ui-state-active');
        		}
                monthpicker.trigger('monthpicker-change-year', $(this).val());
            });

            // mount years combo
            for (var i = settings.sYear; i <= settings.finalYear; i++) {
                var option = $('<option class="mtz-monthpicker" />').attr('value', i).append(i);
                if (settings.endYear == i) {
                    option.attr('selected', 'selected');
                }
                combo.append(option);
            }
            header.append(combo).appendTo(container);
            	
            // mount months table
            for (var i=1; i<=12; i++) {
                td = $('<td class="ui-state-default mtz-monthpicker mtz-monthpicker-month mtz-mp-mon-'+i+'" style="padding:5px;cursor:default;" />').attr('data-month',i);
                if (settings.startMonth <= i && settings.endMonth >= i) {
                   td.addClass('ui-state-active');
                }
                td.append(settings.monthNames[i-1]);
                tr.append(td).appendTo(tbody);
                if (i % 3 === 0) {
                    tr = $('<tr class="mtz-monthpicker" />'); 
                }
            }

            tbody.find('.mtz-monthpicker-month').on('click', function () { 
                var m = parseInt($(this).data('month')),
                	y = $(this).closest('.ui-datepicker').find('.mtz-monthpicker-year').first().val();
                if(settings.mode == 'range'){
                	settings.clickNum++;
                	if(settings.clickNum%2 == 0){
                		settings.endMonth = m;
                		settings.endYear = y;
                		var sm = settings.startMonth,sy = settings.startYear;
                		if(sy > y || (sy == y && sm > m)){
                			settings.startMonth = m;
                			settings.endMonth = sm;
                			settings.startYear = y;
                        	settings.endYear = sy;
                		}
                		var s = 0,e = 0;
                		if(sy > y){
                			s = settings.startMonth;e = 12;
                		}
                		if(sy == y){
                			s = settings.startMonth; e = settings.endMonth;
                		}
                		if(sy < y){
                			s = 1;e = settings.endMonth;
                		}
                		for(var i = s; i<= e;i++){
                			$(this).closest('table').find('.mtz-mp-mon-' + i).addClass('ui-state-active');
                		}
                	}else{
                		 $(this).closest('table').find('.ui-state-active').removeClass('ui-state-active');
                		settings.startMonth = m;
            			settings.endMonth = m;
            			settings.startYear = y;
                    	settings.endYear = y;
                	}
                }else{
                	$(this).closest('table').find('.ui-state-active').removeClass('ui-state-active');
                	settings.startMonth = m;
                	settings.endMonth = m;
                	settings.startYear = y;
                	settings.endYear = y;
                }
//                monthpicker.trigger('monthpicker-click-month', $(this).data('month'));
                $(this).addClass('ui-state-active');
            });
            
//            tbody.find('.mtz-monthpicker-month').on('dblclick', function () {
//                var m = parseInt($(this).data('month'));
//                if ($.inArray(m, settings.disabledMonths) < 0 ) {
//                    settings.selectedYear = $(this).closest('.ui-datepicker').find('.mtz-monthpicker-year').first().val();
//                    settings.selectedMonth = $(this).data('month');
//                    settings.selectedMonthName = $(this).text();
//                    monthpicker.trigger('monthpicker-click-month', $(this).data('month'));
//                    $(this).closest('table').find('.ui-state-active').removeClass('ui-state-active');
//                    $(this).addClass('ui-state-active');
//                }
//            });

            table.append(tbody).appendTo(container);
            
            okBtn.appendTo(container);
            
            okBtn.click(function(){
            	monthpicker.trigger('monthpicker-click-month');
            })
            
            container.appendTo('body');
        },

        destroy: function () {
            return this.each(function () {
                $(this).removeClass('mtz-monthpicker-widgetcontainer').unbind('focus').removeData('monthpicker');
            });
        }, 

        parseInputValue: function (settings) {
            if (this.val()) {
                if (settings.dateSeparator) {
                	var vals = this.val().toString().split(" - ");
                	var sval = vals[0].split(settings.dateSeparator);
                	settings.startYear = parseInt(sval[0],10);
                	settings.endYear = parseInt(sval[0],10);
                	settings.startMonth = parseInt(sval[1],10);
                	settings.endMonth = parseInt(sval[1],10);
                	if(vals.length > 1){
                		var vs = vals[1].split(settings.dateSeparator);
                		settings.endYear = parseInt(vs[0],10);
                		settings.endMonth = parseInt(vs[1],10);
                	}
                }
            }
        }

    };

    $.fn.monthpicker = function (method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call( arguments, 1 ));
        } else if (typeof method === 'object' || ! method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.mtz.monthpicker');
        }    
    };

})(jQuery);
