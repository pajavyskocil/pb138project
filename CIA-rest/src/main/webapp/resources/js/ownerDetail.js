if (! $('#preview').getAttribute('src')) {
    $('#preview').css('visiblity', 'gone');
}

$('#logo').change(function () {
    var input = $(this);
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#preview').attr('src', e.target.result)
                .css('visiblity', 'visible');
        };

        reader.readAsDataURL(input.files[0]);
    }
});