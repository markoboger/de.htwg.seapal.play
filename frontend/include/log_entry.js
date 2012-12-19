/* boat_info.js */

$(document).ready(function() {

	$('#form').submit(function(event) {
		event.preventDefault();
		var tripId = $('#trip_id').val();
		$.ajax({
			type: "POST",
			url: $(this).attr('action'),
			data: $(this).serialize(),
			dataType: "json",
			success: function(data) {
				if(data.success){
					$('#addSuccessModal').modal('show');
					window.location.href = 'trip_info.php?trip=' + tripId;
				}else{
					console.log(data.errors);
					alert("Serverside error occured!");
				}
			}
		});
	});

	function populateJSON( item, data){
		var $inputs = $(item);
		$.each(data, function(key, value) {
		  $inputs.filter(function() {
		    return key == this.name;
		  }).val(value);
		});
	}

	function loadWaypoint( itemId) {
		$.ajax({
			type: "GET",
			url: $('#form').attr('action'),
			data: {id: itemId},
			dataType: "json",
			success: function(item) {
				populateJSON('#form input', item);
				$('html, body').animate({ scrollTop: 0 }, 600);
			}
		});
	}
});

