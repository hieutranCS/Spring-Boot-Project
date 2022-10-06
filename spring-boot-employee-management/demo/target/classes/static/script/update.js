function phonePlaceHolder() {
  var num = $(this).val().replace(/\D/g, "");
  $(this).val(
    "(" +
      num.substring(0, 3) +
      ")" +
      num.substring(3, 6) +
      "-" +
      num.substring(6, 10)
  );
}
$('[type="tel"]').keyup(phonePlaceHolder);

function dobPlaceHolder() {
  var num = $(this).val().replace(/\D/g, "");
  $(this).val(
    num.substring(0, 2) + "/" + num.substring(2, 4) + "/" + num.substring(4, 8)
  );
}
$('#dob').keyup(dobPlaceHolder);

function dhPlaceHolder() {
  var num = $(this).val().replace(/\D/g, "");
  $(this).val(
    num.substring(0, 2) + "/" + num.substring(2, 4) + "/" + num.substring(4, 8)
  );
}
$('#dateHire').keyup(dhPlaceHolder);

function zipcodePlaceHolder() {
  var num = $(this).val().replace(/\D/g, "");
  $(this).val( num.substring(0, 5));
}
$('#zipcode').keyup(zipcodePlaceHolder);