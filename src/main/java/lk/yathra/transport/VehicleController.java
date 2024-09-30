package lk.yathra.transport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.yathra.privilege.Privilege;
import lk.yathra.privilege.PrivilegeController;
import lk.yathra.user.UserDao;

@RestController
public class VehicleController {

    @Autowired
    private VehicleDao vehiDao;

    @Autowired
    private VehicleStatusDao vSttsDao;

    @Autowired
    private VehicleTypeDao vehicleTypeDao;

    @Autowired
    private PrivilegeController prvcntrler;

    @Autowired
    private UserDao userDao;

    // mapping for get the vehicle UI
    @RequestMapping(value = "/vehicle", method = RequestMethod.GET)
    public ModelAndView VehiUi() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ModelAndView vehicleView = new ModelAndView();
        vehicleView.setViewName("vehicle.html");
        vehicleView.addObject("loggedusername", auth.getName());
        vehicleView.addObject("title", "Yathra Vehicles");

        return vehicleView;
    }

    // mapping for get vehicle all data
    @GetMapping(value = "/vehicle/alldata", produces = "application/json")
    public List<Vehicle> getAllVehicleData() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "TRANSPORT");

        if (!loggedUserPrivilege.getPrivselect()) {
            return new ArrayList<Vehicle>();
        }

        return vehiDao.findAll(Sort.by(Direction.DESC, "id"));
    }

    // get available vehicle list
    @GetMapping(value = "vehi/availablevehicles/{startDate}/{endDate}", produces = "application/JSON")
    public List<Vehicle> getAvailableVehicles(@PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate) {

        return vehiDao.getAvailableVehicleList(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    // same as abovve but also with vehicle Type
    @GetMapping(value = "vehi/availablevehiclesbyvehitype/{startDate}/{endDate}/{vehitype}", produces = "application/JSON")
    public List<Vehicle> getAvailableVehicles(@PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate, @PathVariable("vehitype") Integer vehitype) {

        return vehiDao.getAvailableVehicleListByVehiType(LocalDate.parse(startDate), LocalDate.parse(endDate),
                vehitype);
    }

    // get previously rented vehicles
    @GetMapping(value = "vehi/rentedvehicles", produces = "application/JSON")
    public List<Vehicle> getRentedVehicles() {
        return vehiDao.getPreviousRentalVehicleList();
    }

    // rented also by vehi type
    @GetMapping(value = "vehi/previousrentalvehicles/{vehitypeId}", produces = "application/JSON")
    public List<Vehicle> getPreviousRentalVehicles(@PathVariable("vehitypeId") Integer vehitypeId) {
        return vehiDao.getPreviousRentalVehicleListByType(vehitypeId);
    }

    // POST mapping for add button/ save employee data
    @PostMapping(value = "/vehicle")
    public String saveVehicle(@RequestBody Vehicle vehicle) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "TRANSPORT");

        if (!loggedUserPrivilege.getPrivinsert()) {
            return "Save Not Completed You Dont Have Permission";
        }

        // check licence duplications
        Vehicle existingVehicle = vehiDao.getVehiByLNum(vehicle.getLicencenumber());
        if (existingVehicle != null) {
            return "Save Not Completed, This Vehicle Already Exists";
        }

        try {

            vehicle.setAddeddatetime(LocalDateTime.now());
            vehicle.setAddeduserid(userDao.getByUName(auth.getName()).getId());
            vehiDao.save(vehicle);
            return "OK";

        } catch (Exception e) {
            return "Save Not Completed : " + e.getMessage();
        }
    }

    // delete mapping for delete button
    @DeleteMapping(value = "/vehicle")
    public String deleteVehicle(@RequestBody Vehicle vehi) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "TRANSPORT");

        if (!loggedUserPrivilege.getPrivdelete()) {
            return "Delete Not Completed : You Dont Have Permission";
        }

        // check if the vehicle exist or not
        Vehicle isVehiExist = vehiDao.getReferenceById(vehi.getId());
        if (isVehiExist == null) {
            return "Delete Not Completed ; Vehicle Not Found";
        }

        try {
            // hard delete
            // vehiDao.delete(vehiDao.getReferenceById(vehi.getId()));

            // soft delete
            VehiStatus deleteVStatus = vSttsDao.getReferenceById(4);
            isVehiExist.setVehistatus_id(deleteVStatus);

            isVehiExist.setDeleteddatetime(LocalDateTime.now());
            isVehiExist.setDeleteduserid(userDao.getByUName(auth.getName()).getId());

            vehiDao.save(isVehiExist);

            return "OK";

        } catch (Exception e) {
            return "Delete Not Completed " + e.getMessage();
        }
    }

    // put mapping for update button
    @PutMapping(value = "/vehicle")
    public String updateVehi(@RequestBody Vehicle vehicle) {
        // user auth

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Privilege loggedUserPrivilege = prvcntrler.getPrivilegesByUserAndModule(auth.getName(), "TRANSPORT");

        if (!loggedUserPrivilege.getPrivupdate()) {
            return "Save Not Completed You Dont Have Permission";
        }

        // check existence
        Vehicle isVehiExist = vehiDao.getReferenceById(vehicle.getId());
        if (isVehiExist == null) {
            return "Update Not Completed ; Vehicle Not Found";
        }

        // check licence duplications
        Vehicle isVehiExistByLicenceNum = vehiDao.getVehiByLNum(vehicle.getLicencenumber());
        if (isVehiExistByLicenceNum != null &&
                isVehiExistByLicenceNum.getId() != vehicle.getId()) {
            return " A Vehicle With This Licence Number Already Exists In The System";
        }

        try {
            vehicle.setLastmodifieddatetime(LocalDateTime.now());
            vehicle.setLastmodifieduserid(userDao.getByUName(auth.getName()).getId());
            vehiDao.save(vehicle);
            return "OK";

        } catch (Exception e) {
            return "Update Not Completed ; " + e.getMessage();
        }

    }

    // update charge per km and vehicle type name
    @PutMapping(value = "/vehitypeupdate")
    public String updateVehiType(@RequestBody VehiType vehitype) {

        // duplication name balanna try ekata kalin

        try {
            if (vehicleTypeDao.existsById(vehitype.getId())) {
                vehicleTypeDao.save(vehitype);
                return "OK";
            } else {
                return "Vehicle Type not found";
            }
        } catch (Exception e) {
            return "Update Not Completed ; " + e.getMessage();
        }
    }
}
