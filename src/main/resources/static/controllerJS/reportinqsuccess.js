window.addEventListener('load', () => {

    //load date pickers when page is loading
    const today = new Date().toISOString().split('T')[0];
    const startDatePicker = document.getElementById('startDatePickerID');
    const endDatePicker = document.getElementById('endDatePickerID');

    // set max attributes for the date pickers
    startDatePicker.setAttribute('max', today);
    endDatePicker.setAttribute('max', today);

    // reset the end date to today's date each time the start day changes
    startDatePicker.addEventListener('change', function () {
        const startDateValue = this.value;
        endDatePicker.value = today;
        endDatePicker.setAttribute('min', startDateValue);
    });

    // end date cannot go earlier than start date
    endDatePicker.addEventListener('change', function () {
        const endDateValue = this.value;
        const startDateValue = startDatePicker.value;
        if (endDateValue < startDateValue) {
            alert('End date cannot be earlier than start date.');
            this.value = '';
        }
    });

});

//get all inqs
const getAllInqCount = () => {
    const startDate = startDatePickerID.value;
    const lastDate = endDatePickerID.value;

    const allInqs = ajaxGetRequest("emp/availabledriver/" + startDate + "/" + lastDate)
    console.log(allInqs);
    // let availableDriversCount = allInqs.length;  // length one na kelimnma count ekak ganne api
    totalInquiries.innerText = allInqs;

}

const fillTable = () => {
    const startDate = startDatePickerID.value;
    const lastDate = endDatePickerID.value;

    if (startDate && lastDate) {

         // Show the table
         document.getElementById('inquiryTable').classList.remove('d-none');

        //fill date range row
        const formattedDateRangeText = `${new Date(startDate).toLocaleDateString()} - ${new Date(lastDate).toLocaleDateString()}`;
        document.getElementById('dateRange').innerText = formattedDateRangeText;

        //get all inqs
        const allInqs = ajaxGetRequest("report/allinqsbygivendate/" + startDate + "/" + lastDate)
        // let availableDriversCount = allInqs.length;  // length one na kelimnma count ekak ganne api
        totalInquiries.innerText = allInqs;

        //get success inqs
        const successInqs = ajaxGetRequest("report/confirmedinqs/" + startDate + "/" + lastDate)
        successfulInquiries.innerText = successInqs;

       let successRate;
       if (allInqs == 0) {
           successRate = 'N/A';
       } else if (successInqs == 0) {
           successRate = '0.00%'; 
       } else {
           successRate = ((successInqs / allInqs) * 100).toFixed(2) + '%';
       }
       successRateId.innerText = successRate;

       
    } else {
        alert('Please select both start date and end date.');
    }
}


