window.addEventListener('load', () => {

    // loggedUserPrivileges = ajaxGetRequest("/privilege/bymodule/STAY");

    refreshPaymentTable();

    refreshPaymentForm();
});

const refreshPaymentTable = () => {

    payments = ajaxGetRequest("/payment/alldata");

    const displayProperty =
        [
            { dataType: 'text', propertyName: 'paycode' },
            { dataType: 'function', propertyName: getBookingId },
            { dataType: 'function', propertyName: getClientNameNContact },
            { dataType: 'text', propertyName: 'paidamount' },
            { dataType: 'function', propertyName: getPaymentStatus }

        ]

    fillDataIntoTable3(paymentMainTable, payments, displayProperty, buttonVisibility = true,)

    $('#paymentMainTable').dataTable();
}

//
const getBookingId = (ob) => {
    return ob.booking_id.bookingcode;
}

//
const getClientNameNContact = (ob) => {
    return ob.booking_id.client_id.clientname + "</br>" + ob.booking_id.client_id.clientname.contactnum;
}

//
const getPaymentStatus = (ob) => {
    return ob.paymentstatus_id.name;
}

//formm refresh fn
const refreshPaymentForm = () => {
    paymentObj = new Object;

    paymentMgtForm.reset();

    //get pending booking codes
    pendingBookings = ajaxGetRequest("/booking/pendings");
    fillDataIntoSelect(bookingCodeSelect, 'Please Select Booking Code', pendingBookings, 'bookingcode');

    //get paackages list
    //auto fill
    paymentPkgSelect.disabled = true;
    // pkgList = ajaxGetRequest("tourpackage/alldata");
    // fillDataIntoSelect2(paymentPkgSelect, "Please Select Package", pkgList, "packagecode", "packagename", paymentObj.booking_id.tourpackage_id.packagecode, paymentObj.booking_id.tourpackage_id.packagename)

    //get stasuses list
    statusList = ajaxGetRequest("/paymentstatus/alldata");
    fillDataIntoSelect(payStatusSelect, 'Please Select The Status', statusList, 'name');

    bookingCodeSelect.style.border = "1px solid #ced4da";
    paymentPkgSelect.style.border = "1px solid #ced4da";
    paidAmountInput.style.border = "1px solid #ced4da";
    payStatusSelect.style.border = "1px solid #ced4da";
    paymentNote.style.border = "1px solid #ced4da";

    refreshPaymentTable();

    paymentUpdateBtn.disabled = true;
    paymentUpdateBtn.style.cursor = "not-allowed";

}

//for add button
const addPayment = () => {

    //check errors
    const errors = checkPaymentFormErrors();

    if (errors == '') {
        const userConfirm = confirm('Are You Sure To Add ?')

        if (userConfirm) {

            //call POST service
            let postServiceResponse = ajaxRequest("/payment", "POST", paymentObj);

            if (postServiceResponse == "OK") {
                alert("Succesfully Saved !!!");
                refreshPaymentTable();
                paymentMgtForm.reset();

                refreshPaymentForm();
                $("#modalPayment").modal("hide");

            } else {
                alert("An Error Occured " + postServiceResponse);
            }
        } else {
            alert('Operation Cancelled By User')
        }
    } else {
        alert('Form Has Followimg Errors \n \n' + errors);
    }

}

const checkPaymentFormErrors = () => {
    let errors = '';

    if (paymentObj.booking_id == null) {
        errors = errors + " Please Select Booking Code \n";
    }

    if (paymentObj.paidamount == null) {
        errors = errors + " Please Enter The Paid Amount \n";
    }

    if (paymentObj.trx_proof == null) {
        errors = errors + " Please Upload The Payment Proof Sent By Custmer\n";
    }

    if (paymentObj.paymentstatus_id == null) {
        errors = errors + " Please Select The Status \n";
    }

    // if (paymentObj.paidamount == null) {
    //     errors = errors + " Please Enter The Paid Amount \n";
    // }

    return errors;
}

//to update button
const refillPayForm = (ob) => {

    paymentObj = JSON.parse(JSON.stringify(ob));
    paymentOldObj = JSON.parse(JSON.stringify(ob));

    $('#modalPayment').modal('show');

    //get pending booking codes
    pendingBookings = ajaxGetRequest("/booking/pendings");
    fillDataIntoSelect(bookingCodeSelect, 'Please Select Booking Code', pendingBookings, 'bookingcode', paymentObj.booking_id.bookingcode);

    pkgList = ajaxGetRequest("tourpackage/alldata");
    fillDataIntoSelect(paymentPkgSelect, "Please Select Package", pkgList, "packagecode", paymentObj.booking_id.tourpackage_id.packagecode)

    paymentPkgSelect.disabled = true;

    statusList = ajaxGetRequest("/paymentstatus/alldata");
    fillDataIntoSelect(payStatusSelect, 'Please Select The Status', statusList, 'name', paymentObj.paymentstatus_id.name);

    bookingCodeSelect.style.border = "1px solid #ced4da";
    paymentPkgSelect.style.border = "1px solid #ced4da";
    paidAmountInput.style.border = "1px solid #ced4da";
    payStatusSelect.style.border = "1px solid #ced4da";
    paymentNote.style.border = "1px solid #ced4da";

    if (paymentObj.trx_proof != null) {
        trxProofImageID.src = atob(paymentObj.trx_proof);
    } else {
        trxProofImageID.src = 'resources/images/sigiriya.jpg';
    }


    paymentAddBtn.disabled = true;
    paymentAddBtn.style.cursor = "not-allowed";

    paymentUpdateBtn.disabled = false;
    paymentUpdateBtn.style.cursor = "pointer";
}

const setbookingIdAndChanges = () => {
    //get paackages list
    //auto fill
    selectDynamicVal(bookingCodeSelect, '', 'paymentObj', 'booking_id');

    clientNameAuto.value = paymentObj.booking_id.client_id.clientname;
    clientContactAuto.value = paymentObj.booking_id.client_id.contactnum;
    totalAmountAuto.value = paymentObj.booking_id.totalamount;

    pkgList = ajaxGetRequest("tourpackage/alldata");
    // fillDataIntoSelect2(paymentPkgSelect, "Please Select Package", pkgList, "packagecode", "packagename", paymentObj.booking_id.tourpackage_id.packagecode)
    fillDataIntoSelect(paymentPkgSelect, "Please Select Package", pkgList, "packagecode", paymentObj.booking_id.tourpackage_id.packagecode)

    console.log('paymentObj.booking_id', paymentObj.booking_id.bookingcode);

    clientNameAuto.value = paymentObj.booking_id.client_id.clientname;
    clientContactAuto.value = paymentObj.booking_id.client_id.contactnum;
    totalAmountAuto.value = paymentObj.booking_id.totalamount;
}

//to update button
const btnUpdatePaymentForm = () => {
    let errors = checkPaymentFormErrors();
    if (errors == "") {
        let updates = checkPayUpdates();
        if (updates == "") {
            alert("No changes detected");
        } else {
            let userConfirm = confirm("Are you sure to proceed ? \n " + updates);

            if (userConfirm) {
                let putServiceResponce = ajaxRequest("/payment", "PUT", paymentObj);

                if (putServiceResponce == "OK") {
                    alert("Successfully Updted");
                    $("#modalPayment").modal("hide");
                    paymentMgtForm.reset();
                    refreshPaymentTable();
                    refreshPaymentForm();


                } else {
                    alert("An Error Occured " + putServiceResponce);
                }
            } else {
                alert("Operation cancelled by the Operator");
            }
        }
    } else {
        alert("There is an unfilled field \n" + errors);
    }
}

///check updates when update button clicks
const checkPayUpdates = () => {
    let updates = "";


    if (paymentObj.paidamount != paymentOldObj.paidamount) {
        updates = updates + " Paid Amount has changed \n";
    }

    if (paymentObj.note != paymentOldObj.note) {
        updates = updates + " Note has changed \n";
    }

    if (paymentObj.booking_id.bookingcode != paymentOldObj.booking_id.bookingcode) {
        updates = updates + "Selected Booking Id has changed \n";
    }

    if (paymentObj.paymentstatus_id.name != paymentOldObj.paymentstatus_id.name) {
        updates = updates + " Booking Status has changed \n";
    }

    if (paymentObj.trx_proof != paymentOldObj.trx_proof) {
        updates = updates + " Payment Proof Image has changed";
    }

    return updates;
}

//to upload images
// const imgValidatorForPayment = (fileElement, object, imgProperty, imgNameProperty, previewId) => {

//     if (fileElement.files != null) {
//         console.log(fileElement.files);

//         let file = fileElement.files[0];
//         window[object][imgNameProperty] = file.name;

//         let fileReader = new FileReader();

//         fileReader.onload = function (e) {
//             previewId.src = e.target.result;
//             window[object][imgProperty] = btoa(e.target.result);

//         }
//         fileReader.readAsDataURL(file);
//     }

// }

//to upload images
const imgValidatorforPayment = (fileElement, object, imgProperty, previewId) => {
    if (fileElement.files && fileElement.files[0]) {
        let file = fileElement.files[0];
        let fileReader = new FileReader();

        fileReader.onload = function (e) {
            previewId.src = e.target.result;
            window[object][imgProperty] = btoa(e.target.result);
        }
        fileReader.readAsDataURL(file);
    }
}

