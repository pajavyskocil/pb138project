$('#btn-add-item').on('click', function() {
    if ($('form')[0].getAttribute('action').indexOf('deleteInvoice') >= 0) {
        return;
    }
    var last = $('.item-record').last()[0];
    var newNodes = $.parseHTML(
        '<div class="row mb-3 item-record">' +
        '   <div class="input-group col-md-11">' +
        '       <input class="col-md-3 form-control" type="text" placeholder="Name" name="itemName[]" required>' +
        '       <input class="col-md-4 form-control" type="text" placeholder="Description" name="itemDesc[]" required>' +
        '       <input class="col-md-2 form-control item-count" type="number" min="1" placeholder="1" name="itemCount[]" required>' +
        '       <input class="col-md-2 form-control item-price" type="number" min="0.01" step="0.01" placeholder="0.01" name="itemPrice[]" required>' +
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

$(document).on('change', '.item-count', function () {
    recalculate();
});

$(document).on('change', '.item-price', function () {
    recalculate();
});

function recalculate() {
    var counts = $('.item-count');
    var prices = $('.item-price');

    var total = 0;
    for (var i = 0; i < counts.length; i++) {
        var plus = Number(counts[i].value) * Number(prices[i].value);
        if (plus <= 0) {
            total = 'error';
            break;
        }
        total += plus;
    }
    $('#price').val(total);
}

function comparePayerAndRecipient(payer, recipient) {
    if (payer === recipient) {
        $('#pbalert').css('display','block');
        $('#submit-form')[0].disabled = true;
    } else {
        $('#pbalert').css('display', 'none');
        $('#submit-form')[0].disabled = false;
    }
}

$('#payer').change(function() {
    var payer = $(this).val();
    var recipient = $('#recipient').val();

    comparePayerAndRecipient(payer, recipient);
});

$('#recipient').change(function() {
    var recipient = $(this).val();
    var payer = $('#payer').val();

    comparePayerAndRecipient(payer, recipient);
});

if ($('form')[0].getAttribute('action') === '/accounting/createInvoice') {
    $('#payer').val(0).change();
    $('#recipient').val(0).change();
}