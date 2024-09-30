// package lk.yathra.dayplans;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.domain.Sort.Direction;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.servlet.ModelAndView;

// import lk.yathra.privilege.Privilege;
// import lk.yathra.privilege.PrivilegeController;
// import lk.yathra.user.UserDao;

// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;

// @RestController
// public class DayPlanController {

//     @Autowired
//     private DayPlanDao dpDao;

//     @Autowired
//     private PrivilegeController prvcntrler;

//     @Autowired
//     private DayPlanStatusDao dpSttsDao;

//     @Autowired
//     private UserDao userDao;

//     // to display the dayplan UI
//     @RequestMapping(value = "/dayplan", method = RequestMethod.GET)
//     public ModelAndView dayplanUI() {

//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//         ModelAndView dayplanView = new ModelAndView();
//         dayplanView.setViewName("dayplan.html");
//         dayplanView.addObject("loggedusername", auth.getName());
//         dayplanView.addObject("title", "Yathra DayPlan");

//         return dayplanView;
//     }

//     // to get all data
//     @GetMapping(value = "/dayplan/alldata", produces = "application/JSON")
//     public List<DayPlan> getDayPlanAllData() {

//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//         Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "DAYPLAN");

//         if (!loggedUserPrivilege.getPrivselect()) {
//             return new ArrayList<DayPlan>();
//         }

//         return dpDao.findAll(Sort.by(Direction.DESC, "id"));
//     }

//     // to support selecting days in your package

//     // filter only first days
//     @GetMapping(value = "/dayplan/onlyfirstdays", produces = "application/json")
//     public List<DayPlan> getFDsOnly() {
//         return dpDao.getOnlyFirstDays();
//     }

//     // filter only middle days
//     @GetMapping(value = "/dayplan/onlymiddays", produces = "application/json")
//     public List<DayPlan> getMDsOnly() {
//         return dpDao.getOnlyMidDays();
//     }

//     // filter only last days
//     @GetMapping(value = "/dayplan/onlylastdays", produces = "application/json")
//     public List<DayPlan> getlDsOnly() {
//         return dpDao.getOnlyLastDays();
//     }

//     // and by the given inquiry
//     @GetMapping(value = "/dayplan/onlyfirstdays/{basedInqDP}", produces = "application/json")
//     public List<DayPlan> getFDsByInquiry(@PathVariable("basedInqDP") String basedInqDP) {
//         return dpDao.getOnlyFirstDaysAlsoBelongsToGivenInquiry(basedInqDP);
//     }

//     // and by the given inquiry
//     @GetMapping(value = "/dayplan/onlymiddays/{basedInqDP}", produces = "application/json")
//     public List<DayPlan> getMDsByInquiry(@PathVariable("basedInqDP") String basedInqDP) {
//         return dpDao.getOnlyMidDaysAlsoBelongsToGivenInquiry(basedInqDP);
//     }

//     // and by the given inquiry
//     @GetMapping(value = "/dayplan/onlylastdays/{basedInqDP}", produces = "application/json")
//     public List<DayPlan> getLDsByInquiry(@PathVariable("basedInqDP") String basedInqDP) {
//         return dpDao.getOnlyLastDaysAlsoBelongsToGivenInquiry(basedInqDP);
//     }

//     // to save dayplan data
//     @PostMapping(value = "/dayplan")
//     public String saveDayPlaninfo(@RequestBody DayPlan dplan) {

//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//         Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "DAYPLAN");

//         if (!loggedUserPrivilege.getPrivinsert()) {
//             return "Save Not Completed You Dont Have Permission";
//         }

//         try {
//             dplan.setAddeddatetime(LocalDateTime.now());
//             dplan.setAddeduserid(userDao.getByUName(auth.getName()).getId());

//             // Generate the initial dayplancode
//             String nextCode;
//             List<DayPlan> codeCountByDistrict = dpDao.getDayPlansListBySD(dplan.getStart_district_id().getId());
//             if (codeCountByDistrict.size() == 0) {
//                 nextCode = dplan.getDayplancode() + dplan.getStart_district_id().getName().substring(0, 3).toUpperCase()
//                         + "1";
//             } else {
//                 nextCode = dplan.getDayplancode() + dplan.getStart_district_id().getName().substring(0, 3).toUpperCase()
//                         + (codeCountByDistrict.size() + 1);
//             }

//             // Ensure the generated code is unique
//             while (dpDao.existsByDayplancode(nextCode)) {
//                 // Increment the code and check again
//                 int number = Integer.parseInt(nextCode.replaceAll("\\D+", "")); // Extract current number
//                 number++;
//                 nextCode = nextCode.replaceAll("\\d+$", Integer.toString(number)); // Replace with new number
//             }

//             dplan.setDayplancode(nextCode);
//             dpDao.save(dplan);
//             return nextCode;

//         } catch (Exception e) {
//             return "save not completed : " + e.getMessage();

//         }

//     }

//     // to save the same record but with new attributes
//     @PostMapping(value = "/dayplan/saveasnew")
//     public String DayPlanSaveAsNew(@RequestBody DayPlan dplan) {

//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//         Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "DAYPLAN");

//         if (!loggedUserPrivilege.getPrivinsert()) {
//             return "Save Not Completed You Dont Have Permission";
//         }

//         try {

//             dplan.setAddeddatetime(LocalDateTime.now());
//             dplan.setAddeduserid(userDao.getByUName(auth.getName()).getId());

//             // String nextCode = null;
//             // if day type hasnt changed (FD/MD/LD/)
//             if (dplan.getDayplancode().length() > 2) {
//                 String dpCurrentCode = dplan.getDayplancode();

//                 // Extract the prefix (non-numeric part) and numeric part
//                 String prefix = dpCurrentCode.replaceAll("\\d+$", ""); // Extracts non-numeric part
//                 String numericPart = dpCurrentCode.replaceAll("\\D+", ""); // Extracts numeric part

//                 // Increment the numeric part
//                 int number = Integer.parseInt(numericPart);
//                 number++;

//                 // Combine the prefix with the incremented number
//                 String nextCode = prefix + number;
//                 dplan.setDayplancode(nextCode);
//                 return nextCode;

//             } else {
//                 // get the first 2 letters of prefix part(FD/MD/LD/)
//                 // Generate the initial dayplancode
//                 String nextCode;
//                 List<DayPlan> codeCountByDistrict = dpDao.getDayPlansListBySD(dplan.getStart_district_id().getId());
//                 if (codeCountByDistrict.size() == 0) {
//                     nextCode = dplan.getDayplancode()
//                             + dplan.getStart_district_id().getName().substring(0, 3).toUpperCase()
//                             + "1";
//                 } else {
//                     nextCode = dplan.getDayplancode()
//                             + dplan.getStart_district_id().getName().substring(0, 3).toUpperCase()
//                             + (codeCountByDistrict.size() + 1);
//                 }

//                 // Ensure the generated code is unique
//                 while (dpDao.existsByDayplancode(nextCode)) {
//                     // Increment the code and check again
//                     int number = Integer.parseInt(nextCode.replaceAll("\\D+", "")); // Extract current number
//                     number++;
//                     nextCode = nextCode.replaceAll("\\d+$", Integer.toString(number)); // Replace with new number
//                 }

//                 dplan.setDayplancode(nextCode);

//             }

//             // dplan.setDayplancode(nextCode);
//             dplan.setId(null);

//             dpDao.save(dplan);
//             return nextCode;

//         } catch (Exception e) {
//             return "Save Not Completed : " + e.getMessage();

//         }

//     }

//     // to update dayplan data
//     @PutMapping(value = "/dayplan")
//     public String updateDayPlan(@RequestBody DayPlan dplan) {

//         // user auth
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//         Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "DAYPLAN");

//         if (!loggedUserPrivilege.getPrivupdate()) {
//             return "Update Not Completed You Dont Have Permission";
//         }

//         try {
//             dplan.setLastmodifieddatetime(LocalDateTime.now());
//             dplan.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());
//             dpDao.save(dplan);
//             return "OK";
//         } catch (Exception e) {
//             return "Update Not Completed ; " + e.getMessage();
//         }
//     }

//     // to simulate the deletion of dayplan data
//     @DeleteMapping(value = "/dayplan")
//     public String deleteDayPlan(@RequestBody DayPlan dplan) {

//         // user auth
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//         Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "DAYPLAN");

//         if (!loggedUserPrivilege.getPrivdelete()) {
//             return "Delete Not Completed : You Dont Have Permission";
//         }

//         // check existence
//         DayPlan existDayPlan = dpDao.getReferenceById(dplan.getId());
//         if (existDayPlan == null) {
//             return "Delete Not Completed, Record Not Found";
//         }

//         try {
//             existDayPlan.setDeleteddatetime(LocalDateTime.now());
//             existDayPlan.setDeleteduserid(userDao.getByUName(auth.getName()).getId());

//             DayPlanStatus deleteStatus = dpSttsDao.getReferenceById(2);
//             existDayPlan.setDpstatus_id(deleteStatus);
//             dpDao.save(existDayPlan);
//             return "OK";

//         } catch (Exception e) {
//             return "Delete Not Completed : " + e.getMessage();
//         }

//     }

// }
