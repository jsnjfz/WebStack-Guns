(function () {
	var readCookie = function (name) {
		var prefix = name + "=";
		var parts = document.cookie ? document.cookie.split(";") : [];
		for (var i = 0; i < parts.length; i++) {
			var part = parts[i].replace(/^\s+/, "");
			if (part.indexOf(prefix) === 0) {
				return decodeURIComponent(part.substring(prefix.length));
			}
		}
		return "";
	};

	window.getCsrfToken = function () {
		return readCookie("XSRF-TOKEN");
	};

	var isSameOrigin = function (url) {
		var target = document.createElement("a");
		target.href = url || window.location.href;
		return target.protocol === window.location.protocol
			&& target.host === window.location.host;
	};

	// Bootstrap Table、Tree Table 等组件会直接调用 $.ajax，统一为同源请求补上 CSRF 请求头。
	$(document).off("ajaxSend.gunsCsrf").on("ajaxSend.gunsCsrf", function (event, xhr, settings) {
		var token = window.getCsrfToken();
		if (token && isSameOrigin(settings.url)) {
			xhr.setRequestHeader("X-CSRF-TOKEN", token);
		}
	});

	var $ax = function (url, success, error) {
		this.url = url;
		this.type = "post";
		this.data = {};
		this.dataType = "json";
		this.async = false;
		this.success = success;
		this.error = error;
	};
	
	$ax.prototype = {
		start : function () {	
			var me = this;
			
			if (this.url.indexOf("?") == -1) {
				this.url = this.url + "?jstime=" + new Date().getTime();
			} else {
				this.url = this.url + "&jstime=" + new Date().getTime();
			}
			
			$.ajax({
		        type: this.type,
		        url: this.url,
		        dataType: this.dataType,
		        async: this.async,
		        data: this.data,
				beforeSend: function(xhr) {
					var token = window.getCsrfToken();
					if (token) {
						xhr.setRequestHeader("X-CSRF-TOKEN", token);
					}
				},
		        success: function(data) {
		        	me.success(data);
		        },
		        error: function(data) {
		        	me.error(data);
		        }
		    });
		}, 
		
		set : function (key, value) {
			if (typeof key == "object") {
				for (var i in key) {
					if (typeof i == "function")
						continue;
					this.data[i] = key[i];
				}
			} else {
				this.data[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
			}
			return this;
		},
		
		setData : function(data){
			this.data = data;
			return this;
		},
		
		clear : function () {
			this.data = {};
			return this;
		}
	};
	
	window.$ax = $ax;
	
} ());
