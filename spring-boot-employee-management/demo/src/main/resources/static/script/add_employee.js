(() => {
  "use strict";
  const forms = document.querySelectorAll(".needs-validation");

  Array.from(forms).forEach((form) => {
    form.addEventListener(
      "submit",
      (event) => {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }

        form.classList.add("was-validated");
      },
      false
    );
  });
})();

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