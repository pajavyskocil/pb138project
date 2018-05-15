$('#btn-add-item').on('click', function() {
    if ($('form')[0].getAttribute('action').indexOf('deleteInvoice') >= 0) {
        return;
    }
    var last = $('.item-record').last()[0];
    var newNodes = $.parseHTML(
        '<div class="row mb-3 item-record">' +
        '   <div class="input-group col-md-11">' +
        '       <input class="col-md-3 form-control" type="text" placeholder="Name" name="itemName[]" required>' +
        '       <input class="col-md-6 form-control" type="text" placeholder="Description" name="itemDesc[]" required>' +
        '       <input class="col-md-1 form-control" type="text" placeholder="Count" name="itemCount[]" required>' +
        '       <input class="col-md-1 form-control item-price" type="text" placeholder="Price" name="itemPrice[]" required>' +
        '       <div class="col-md-1 no-padd input-group-append">' +
        '           <span class="input-group-text">&euro;</span>' +
        '       </div>' +
        '   </div>' +
        '   <div class="col-md-1">' +
        '       <span class="btn btn-danger btn-remove-item"><i class="fas fa-minus"></i></span>' +
        '   </div>' +
        '</div>');


    $(last).removeClass('mb-3');
    $(last).addClass('mb-2');
    $('#items-after')[0].before(newNodes[0]);
});

$(document).on('click', '.btn-remove-item', function() {
    if ($('form')[0].getAttribute('action').indexOf('deleteInvoice') >= 0) {
        return;
    }
    if ($('.item-record').length > 1) {
        $(this).parent().parent().remove();
    }

    var last = $('.item-record').last()[0];
    $(last).removeClass('mb-2').addClass('mb-3');
});

$('#payer').change(function(){
    var payer = $(this).val();
    var buyer = $('#recipient').val();

    if (payer === buyer) {
        $('#pbalert').css('display','block');
        $('#submit-form')[0].setAttribute('disabled', 'disabled');
    } else {
        $('#pbalert').css('display', 'none');
        $('#submit-form')[0].removeAttribute('disabled');
    }
});

$('#recipient').change(function(){
    var payer = $(this).val();
    var buyer = $('#recipient').val();

    if (payer === buyer) {
        $('#pbalert').css('display','block');
        $('#submit-form')[0].setAttribute('disabled', 'disabled');
    } else {
        $('#pbalert').css('display', 'none');
        $('#submit-form')[0].removeAttribute('disabled');
    }
});