/*
 * styleCombobox 1.0 - Grifo
 * http://www.grifotecnologia.com.br/
 * Date: 2010
 */

(function($) {

	$.fn.extend({

		styleForm: function(options) {
			
			//SELECT - COMBOBOX////////
			
			// IE6
			if ($.browser && $.browser.msie && parseInt($.browser.version) == 6 && typeof window['XMLHttpRequest'] != "object")
				return;

			// Init
			var $selector = $(this).find('select');
				classFocus = 'inputFocus';
				
			$selector.each(function() {
				var $combo = $(this),
					initialValue = $combo.find("option:selected").text();
					/*$combo.attr("title") ? $combo.attr("title") : */	
				$combo.css({
						   position:'relative', 
						   opacity:0,
						   filter:'alpha(opacity=0)'
						   });
				$combo.wrap('<div class="styleCombobox skin" />');
				var largCombo = $combo.parent('.styleCombobox').width()-30;
				$combo.before('<span class="skin" style="width:'+largCombo+'px">'+ initialValue +'</span>');
				
			});

			$selector.bind("change keypress keydown keyup",function() {
				$(this).prev().html($(this).find("option:selected").text());
			});

			$selector.focus(function() {
				$(this).parent().addClass(classFocus);
			}).blur(function() {
				$(this).parent().removeClass(classFocus);
			});
			
			//INPUT FILE///////
			
			// Init
			var $inputfile = $(this).find('input:file');
			/*var larg = $inputfile.width()+'px';
			if(navigator.appName == 'Microsoft Internet Explorer')
				var alt = ($inputfile.height()+2)+'px';
			else
				var alt = $inputfile.height()+'px';*/
			
			var versao = parseInt(navigator.appVersion);
			var navegador = navigator.appName;
			$inputfile.css({
				position: 'absolute',
				zIndex: 2,
				top: 0,
				left: 0,
				width: '300px',
				opacity: 0,
				filter: 'alpha(opacity=0)'
			});
			//largura autom�tica --> style="width:'+larg+';height:'+alt+'"
			$inputfile.wrap('<div class="styleFile skin" />');
			$inputfile.before('<span class="skin" ></span>');
			
			$inputfile.change(function() {
				var file_val = $(this).val();
				
				if(file_val.length >14)
					var file_name = file_val.substr(0,14)+'...';
				else
					var file_name = file_val;
				
				$(this).prev().html(file_name);
			});
			
			//RADIO & CHECKBOX////////

			var $radiocheck = $(this).find('input:radio,input:checkbox'),
				classFocus = 'inputFocus';
			
			$radiocheck.css({
						   cursor:'pointer',
						   filter:'alpha(opacity=0)',
						   opacity:0
						  });
			$radiocheck.filter(":checkbox").wrap("<div class='style_checkbox skin' />");
			$radiocheck.filter(":radio").wrap("<div class='style_radio skin' />");
			
			var check = function($obj) {
				if ($obj.is(":checked")) {
					$obj.parent().addClass('input_'+$obj.attr('type')+'Checked');
				} else {
					$obj.parent().removeClass('input_'+$obj.attr('type')+'Checked');
				}
			}
			
			//se j� tiver checado
			$radiocheck.each(function() {
				check($(this));
			});

			/*// Checkbox
			$radiocheck.filter(":checkbox").click(function() {	
				var name = $(this).attr("name");

				$radiocheck.filter("input[name='"+name+"']").each(function() {
					check($(this));
				});
			});
			//alert($selector.filter(":radio").length);
			// Radio*/
			$radiocheck.bind('click',function() {
				var name = $(this).attr("name");

				$radiocheck.filter("input[name='"+name+"']").each(function() {
					check($(this));
				});
			});

			$radiocheck.focus(function() {
				$(this).parent().addClass(classFocus);
			}).blur(function() {
				$(this).parent().removeClass(classFocus);
			});

		}
		
	});
})(jQuery);