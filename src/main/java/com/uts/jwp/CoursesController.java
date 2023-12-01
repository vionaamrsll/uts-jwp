package com.uts.jwp;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.uts.jwp.domain.Courses;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
public class CoursesController {
    
    public static Map<String, Courses> courseMap = new HashMap<>();

	@GetMapping("/courses")
    public String getCourses(Model model) {
        model.addAttribute("courses", fetchCourses());
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Courses course) {
        return "addCourses";
    }

	// Add-Course
    @PostMapping("/courses")
    public String addcourse(@Valid Courses course, BindingResult bindingResult, Model model) {

        validateCourseCode(course.getCourseCode(), bindingResult);
        validateSumSKS(course.getSumSKS(), bindingResult);
        validateFaculty(course.getFaculty(), bindingResult);

        if (bindingResult.hasErrors()) {
            return "addCourses";
        }

        if (isCourseCodeAlreadyExists(course.getCourseCode())) {
            throw new IllegalArgumentException("Course with courseCode:" + course.getCourseCode() + " already exists");
        }

        courseMap.put(course.getCourseCode(), course);
        model.addAttribute("courses", fetchCourses());
        return "index";
    }

    private void validateCourseCode(String courseCode, BindingResult bindingResult) {
        if (courseCode == null || !courseCode.matches("^PG\\d{3}$")) {
            ObjectError error = new ObjectError("courseCode", "Code Course must start with PG and end with 3 digits");
            bindingResult.addError(error);
        }
    }

    private void validateSumSKS(Integer sumSKS, BindingResult bindingResult) {
        if (sumSKS == null || sumSKS < 1 || sumSKS > 3) {
            ObjectError error = new ObjectError("sumSKS", "Total SKS must be between 1 and 3");
            bindingResult.addError(error);
        }
    }

    private void validateFaculty(String faculty, BindingResult bindingResult) {
        if (faculty == null || !(faculty.equals("FE") || faculty.equals("FTI") || faculty.equals("FEB") ||
                faculty.equals("FT") || faculty.equals("FISSIP") || faculty.equals("FKDK"))) {
            ObjectError error = new ObjectError("faculty", "Invalid faculty. Choose from: FE, FTI, FEB, FT, FISSIP, FKDK");
            bindingResult.addError(error);
        }
    }

    private boolean isCourseCodeAlreadyExists(String courseCode) {
        return courseMap.containsKey(courseCode);
    }

	@GetMapping(value = "/courses/{courseCode}")
    public ResponseEntity<Courses> findCourses(@PathVariable("courseCode") String courseCode) {
        final Courses courses = courseMap.get(courseCode);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

	private static List<Courses> fetchCourses() {
        return courseMap.values().stream().collect(Collectors.toList());
    }

	// Edit-Course
    @PostMapping(value = "/courses/{courseCode}")
    public String updateCourse(@PathVariable("courseCode") String courseCode,
    @Valid Courses course,
    BindingResult result, Model model) {

        validateSumSKS(course.getSumSKS(), result);
        validateFaculty(course.getFaculty(), result);

        final Courses courseToBeUpdated = courseMap.get(courseCode);
        if (courseToBeUpdated == null) {
        throw new IllegalArgumentException("Course with courseCode:" + courseCode + " not found");
        }

        if (!courseCode.equals(course.getCourseCode()) && isCourseCodeAlreadyExists(course.getCourseCode())) {
        throw new IllegalArgumentException("Course with courseCode:" + course.getCourseCode() + " already exists");
        }

        // Update other fields as needed
        courseMap.put(course.getCourseCode(), course);

        model.addAttribute("courses", fetchCourses());
        return "redirect:/courses";
    }

	@GetMapping("/edit/{courseCode}")
	public String showUpdateForm(@PathVariable("courseCode") String courseCode, Model model) {
		final Courses coursesToBeUpdate = courseMap.get(courseCode);
		if (coursesToBeUpdate == null) {
			throw new IllegalArgumentException("Course with code : " + courseCode + "is not found");

		}
		model.addAttribute("courses", coursesToBeUpdate);
		return "updateCourses";
	}

	// Delete-Course
    @GetMapping(value = "/courses/{courseCode}/delete")
    public String deleteCourse(@PathVariable("courseCode") String courseCode) {
        courseMap.remove(courseCode);
        return "redirect:/courses";
    }

    // private static List<Courses> fetchCourses() {
    //     return List.copyOf(courseMap.values());
    // }

}



// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;



// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.validation.ObjectError;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;


// import lombok.extern.slf4j.Slf4j;
// import javax.validation.Valid;
// import com.uts.jwp.domain.Courses;

// @Controller
// @Slf4j
// public class CoursesController {
//     public static Map<String, Courses> courseMap = new HashMap<>();
    
   
//     @GetMapping("/courses")
//     public String getCourses(Model model) {
//        model.addAttribute("courses", fetchCourses());
//        return "index";
//    }
//     	@GetMapping("/signup")
//     public String showSignUpForm(Courses course) {
//         return "addCourses";
//     }
    
//     @PostMapping("/courses")
//     public String addCourse(@Valid Courses course, BindingResult bindingResult, Model model) {

//         validatecourseCode(course.getcoursesCode(), bindingResult);
//         validatesumSKS(course.getsumSKS(), bindingResult);
//         validatefaculty(course.getfaculty(), bindingResult);

//         if (bindingResult.hasErrors()) {
//             return "addCourses";
//         }

//         if (isCourseCodeAlreadyExists(course.getcoursesCode())) {
//             throw new IllegalArgumentException("Course with courseCode:" + course.getcoursesCode() + " already exists");
//         }

//         courseMap.put(course.getcoursesCode(), course);
//         model.addAttribute("courses", fetchCourses());
//         return "index";
//     }

//     private void validatecourseCode(String courseCode, BindingResult bindingResult) {
//         if (courseCode == null || !courseCode.matches("^PG\\d{3}$")) {
//             ObjectError error = new ObjectError("coursesCode", "Code Course must start with PG and end with 3 digits");
//             bindingResult.addError(error);
//         }
//     }

//     private void validatesumSKS(Integer sumSKS, BindingResult bindingResult) {
//         if (sumSKS == null || sumSKS < 1 || sumSKS > 3) {
//             ObjectError error = new ObjectError("sumSKS", "Total SKS must be between 1 and 3");
//             bindingResult.addError(error);
//         }
//     }

//     private void validatefaculty(String faculty, BindingResult bindingResult) {
//         if (faculty == null || !(faculty.equals("FE") || faculty.equals("FTI") || faculty.equals("FEB") ||
//                 faculty.equals("FT") || faculty.equals("FISSIP") || faculty.equals("FKDK"))) {
//             ObjectError error = new ObjectError("faculty", "Invalid faculty. Choose from: FE, FTI, FEB, FT, FISSIP, FKDK");
//             bindingResult.addError(error);
//         }
//     }

//     private boolean isCourseCodeAlreadyExists(String courseCode) {
//         return courseMap.containsKey(courseCode);
//     }
//     //  @PostMapping("/courses")
//     //     public String addCourses(@Valid Course course, BindingResult bindingResult, Model model) {

//     //     // Validate courses Code
//     //     String errorcourseCode = validateCourseCode(course.getcoursesCode());
//     //     if (errorcourseCode != null) {
//     //         ObjectError error = new ObjectError("globalError", errorcourseCode);
//     //         bindingResult.addError(error);
//     //     }

//     //     // Validate course Name
//     //     String errorcoursesName = validatecoursesName(course.getcoursesName());
//     //     if (errorcoursesName != null) {
//     //         ObjectError error = new ObjectError("globalError", errorcourseCode);
//     //         bindingResult.addError(error);
//     //     }

//     //     // Validate sumSKS
//     //     String errorsumSKS = validatesumSKS(course.getsumSKS());
//     //     if (errorsumSKS != null) {
//     //         ObjectError error = new ObjectError("globalError", errorsumSKS);
//     //         bindingResult.addError(error);
//     //     }
//     //      // Validate faculty
//     //     String errorfaculty = validatefaculty(course.getfaculty());
//     //     if (errorfaculty != null) {
//     //         ObjectError error = new ObjectError("globalError", errorfaculty);
//     //         bindingResult.addError(error);
//     //     }
//     //     return "addCourses";
//     // }

//     // // Prevent duplicate data
//     //     String duplicateDataError = checkDuplicateData(course);
//     //     if (duplicateDataError != null) {
//     //         ObjectError error = new ObjectError("globalError", duplicateDataError);
//     //         bindingResult.addError(error);
//     //     }

//     //     log.info("bindingResult {}", bindingResult);

//     //     if (bindingResult.hasErrors()) {
//     //         return "addCourses";
//     //     }

//     //     String code = course.getcoursesCode();
//     //     boolean exists = courseMap.values().stream()
//     //             .anyMatch(data -> code.equals(data.getcoursesCode()));

//     //     if (exists) {
//     //         throw new IllegalArgumentException("Teacher with ID:" + code + " is already exist");
//     //     }

//     //     courseMap.put(code, course);
//     //     model.addAttribute("teachers", fetchCourses());
//     //     return "index";
//     // }
    
//     // public String validateCourseCode(String courseCode) {
//     //     if (courseCode == null || courseCode.isEmpty()) {
//     //         return "Kode Matakuliah tidak boleh kosong";
//     //     }
    
//     //     if (!courseCode.startsWith("PG")) {
//     //         return "Kode Matakuliah harus diawali dengan 'PG'";
//     //     }
    
//     //     // Sesuaikan pola berdasarkan persyaratan lebih lanjut jika diperlukan
//     //     // Contoh: Anda ingin memeriksa apakah kode mata kuliah memiliki format PG diikuti oleh angka
//     //     if (!courseCode.substring(2).matches("\\d+")) {
//     //         return "Format Kode Matakuliah tidak valid";
//     //     }
    
//     //     return null;
//     // }
//     // private String validatecoursesName(String courseName) {
//     //     if (courseName == null || courseName.isEmpty()) {
//     //         return "Nama Matakuliah tidak boleh kosong";
//     //     }

//     //     int length = courseName.length();
//     //     if (length < 5 || length > 10) {
//     //         return "Nama Matakuliah harus memiliki panjang antara 5 dan 10 karakter";
//     //     }

//     //     return null;
//     // }

//     // private String validatesumSKS(String sumSKS ) {
//     //     if (sumSKS == null || sumSKS.isEmpty()) {
//     //         return "Total SKS tidak boleh kosong";
//     //     }

//     //     try {
//     //         int sks = Integer.parseInt(sumSKS);
//     //         if (sks < 1 || sks > 3) {
//     //             return "Total SKS harus di antara 1 dan 3";
//     //         }
//     //     } catch (NumberFormatException e) {
//     //         return "Total SKS harus menggunakan angka";
//     //     }

//     //     return null;
//     // }
    
//     // private String validatefaculty(String faculty) {
//     //     // Gantilah dengan daftar fakultas yang valid sesuai kebutuhan Anda
//     //     String[] validfaculty = {"FE", "FTI", "FT"};

//     //     if (faculty == null || faculty.isEmpty()) {
//     //         return "Fakultas tidak boleh kosong";
//     //     }

//     //     for (String validfac : validfaculty) {
//     //         if (validfac.equals(faculty)) {
//     //             return null; // Valid
//     //         }
//     //     }

//     //     return "Fakultas tidak valid";
//     // }

    
// 	@GetMapping(value = "/courses/{courseCode}")
//     public ResponseEntity<Courses> findCourse(@PathVariable("courseCode") String courseCode) {
//         final Courses course = courseMap.get(courseCode);
//         return new ResponseEntity<>(course, HttpStatus.OK);
//     }

// 	private static List<Courses> fetchCourses() {
//         return courseMap.values().stream().collect(Collectors.toList());
//     }

//     @PostMapping(value = "/courses/{courseCode}")
//     public String updateCourse(@PathVariable("courseCode") String courseCode,
//             Courses course,
//             BindingResult result, Model model) {
//         final Courses courseToBeUpdated = courseMap.get(course.getcoursesCode());
//         courseToBeUpdated.setcoursesName(course.getcoursesName());
//         courseToBeUpdated.setsumSKS(course.getsumSKS());
//         courseToBeUpdated.setfaculty(course.getfaculty());
//         // Jika ada properti lain yang perlu diupdate, tambahkan di sini

//         courseMap.put(course.getcoursesCode(), courseToBeUpdated);

//         model.addAttribute("courses", fetchCourses());
//         return "redirect:/courses";
//     }

// @GetMapping("/updateCourses/{courseCode}")
//     public String showUpdateForm(@PathVariable("courseCode") String courseCode, Model model) {
//         final Courses courseToBeUpdated = courseMap.get(courseCode);
//         if (courseToBeUpdated == null) {
//             throw new IllegalArgumentException("Course with CourseCode:" + courseCode + " is not found");
//         }
//         model.addAttribute("course", courseToBeUpdated);
//         return "updateCourses";
//     }

// 	@GetMapping(value = "/courses/{courseCode}/delete")
//     public String deleteCourse(@PathVariable("courseCode") String courseCode) {
//         courseMap.remove(courseCode);
//         return "redirect:/courses";
//     }

// }


// 	@GetMapping(value = "/teachers/{nip}")
//     public ResponseEntity<Teacher> findTeacher(@PathVariable("nip") String nip) {
//         final Teacher teacher = teacherMap.get(nip);
//         return new ResponseEntity<>(teacher, HttpStatus.OK);
//     }

// 	private static List<Teacher> fetchTeachers() {
//         return teacherMap.values().stream().toList();
//     }

// 	@PostMapping(value = "/teachers/{nip}")
//     public String updateTeacher(@PathVariable("nip") String nip,
//                                 Teacher teacher,
//                                 BindingResult result, Model model) {
//         final Teacher teacherToBeUpdated = teacherMap.get(teacher.getNip());
//         teacherToBeUpdated.setFullName(teacher.getFullName());
//         teacherToBeUpdated.setEmail(teacher.getEmail());
//         teacherToBeUpdated.setPhoneNumber(teacher.getPhoneNumber());
//         teacherMap.put(teacher.getNip(), teacherToBeUpdated);

//         model.addAttribute("teachers", fetchTeachers());
//         return "redirect:/teachers";
//     }


//     @PostMapping("/courses")
//     public String addCourse(@Valid Course course, BindingResult bindingResult, Model model) {
//     String errorsumSKS = validatesumSKS(course);

//     log.info("errorsumSKS {}", errorsumSKS);
//     if (errorsumSKS != null) {
//         ObjectError error = new ObjectError("globalError", errorsumSKS);
//         bindingResult.addError(error);
//     }

//     log.info("bindingResult {}", bindingResult);

//     if (bindingResult.hasErrors()) {
//         return "addCourse";
//     }
//     String coursesCode = course.getcoursesCode();
//     boolean exists = courseMap.values().stream()
//         .anyMatch(data -> coursesCode.equals(data.getcoursesCode()));

// if (exists) {
//     throw new IllegalArgumentException("Course with course code:" + coursesCode + " is already exist");
// }

// courseMap.put(coursesCode, course);
// model.addAttribute("course", fetchCourses());
// return "index";
//     }

//     private String validatesumSKS(Course course) {
//         String errString = null;
//         if (course.getcoursesCode() == null || !course.getcoursesCode().startsWith("PG")) {
//             errString = "Not Valid. Course code must start with 'PG'";
//         } else {
//             try {
//                 // Validasi total SKS
//                 int sks = Integer.parseInt(course.getsumSKS());
//                 if (sks < 1 || sks > 3) {
//                     errString = "Total SKS must be between 1 and 3";
//                 }
//             } catch (NumberFormatException e) {
//                 errString = "Total SKS must be a number";
        
//             String faculty = course.getfaculty();
//             if (faculty == null || (!faculty.equals("FE") && !faculty.equals("FTI") && !faculty.equals("FT"))) {
//                 errString = "The faculty to be filled in must be one of: FE, FTI, FT";
//             } 
          
//             } return errString;
//         }
//         return errString;
//     }
     
//     @GetMapping(value = "/courses/{coursesCode}")
//     public ResponseEntity<Course> findCourse(@PathVariable("coursesCode") String coursesCode) {
//      final Course course = Course.get(coursesCode);
//         return new ResponseEntity<>(course, HttpStatus.OK);
// }

// private static List<Course> fetchCourses() {
//     return coursesMap.values().stream().collect(Collectors.toList());
// }
// @PostMapping(value = "/courses/{coursesCode}")
// public String updateCourse(@PathVariable("courseCode") String courseCode,
//         Course course,
//         BindingResult result, Model model) {
//     final Course courseToBeUpdated = coursesMap.get(course.getcoursesCode());
//     courseToBeUpdated.setcoursesName(course.getcoursesName());
//     courseToBeUpdated.setsumSKS(course.getsumSKS());
//     courseToBeUpdated.setfaculty(course.getfaculty());
//     // Jika ada properti lain yang perlu diupdate, tambahkan di sini

//     coursesMap.put(course.getcoursesCode(), courseToBeUpdated);

//     model.addAttribute("courses", fetchCourses());
//     return "redirect:/courses";
// }
// @GetMapping("/edit/{coursesCode}")
// public String showUpdateForm(@PathVariable("coursesCode") String coursesCode, Model model) {
//     final Course coursesToBeUpdated = coursesMap.get(coursesCode);
//     if (coursesToBeUpdated == null) {
//         throw new IllegalArgumentException("Courses with course code:" + coursesCode + " is not found");
//     }
//     model.addAttribute("course", coursesToBeUpdated);
//     return "update-course"; // Gantilah dengan nama template yang sesuai untuk update course
// }
// @GetMapping(value = "/courses/{coursesCode}/delete")
// public String deleteCourses(@PathVariable("coursesCode") String coursesCode) {
//     coursesMap.remove(coursesCode);
//     return "redirect:/courses";
// }
// }

//  public static Map<String, Teacher> teacherMap = new HashMap<>();

//     @GetMapping("/teachers")
//     public String getTeachers(Model model) {
//         model.addAttribute("teachers", fetchTeachers());
//         return "index";
//     }

// 	@GetMapping("/signup")
//     public String showSignUpForm(Teacher teacher) {
//         return "addTeachers";
//     }

//     @PostMapping("/teachers")
//     public String addTeacher(@Valid Teacher teacher, BindingResult bindingResult, Model model) {

//         // Validate NIP
//         String errorNIP = validateNIP(teacher.getNip());
//         if (errorNIP != null) {
//             ObjectError error = new ObjectError("globalError", errorNIP);
//             bindingResult.addError(error);
//         }

//         // Validate Email
//         String errorEmail = validateEmail(teacher.getEmail());
//         if (errorEmail != null) {
//             ObjectError error = new ObjectError("globalError", errorEmail);
//             bindingResult.addError(error);
//         }

//         // Validate Phone Number
//         String errorPhoneNumber = validatePhoneNumber(teacher.getPhoneNumber());
//         if (errorPhoneNumber != null) {
//             ObjectError error = new ObjectError("globalError", errorPhoneNumber);
//             bindingResult.addError(error);
//         }

//         // Prevent duplicate data
//         String duplicateDataError = checkDuplicateData(teacher);
//         if (duplicateDataError != null) {
//             ObjectError error = new ObjectError("globalError", duplicateDataError);
//             bindingResult.addError(error);
//         }

//         log.info("bindingResult {}", bindingResult);

//         if (bindingResult.hasErrors()) {
//             return "addTeachers";
//         }

//         String nip = teacher.getNip();
//         boolean exists = teacherMap.values().stream()
//                 .anyMatch(data -> nip.equals(data.getNip()));

//         if (exists) {
//             throw new IllegalArgumentException("Teacher with ID:" + nip + " is already exist");
//         }

//         teacherMap.put(nip, teacher);
//         model.addAttribute("teachers", fetchTeachers());
//         return "index";
//     }

//     private String validateNIP(String nip) {
//         // Check if NIP starts with "LCT" and ends with 10 digits
//         if (!nip.startsWith("LCT") || !nip.substring(3).matches("\\d{10}")) {
//             return "NIP must start with 'LCT' and be followed by 10 digits";
//         }
//         return null;
//     }

//     private String validateEmail(String email) {
//         // Add your email validation logic here
//         // Return an error message if validation fails, otherwise return null
//         return null; // Placeholder, implement your validation logic
//     }

//     private String validatePhoneNumber(String phoneNumber) {
//         // Add your phone number validation logic here
//         // Return an error message if validation fails, otherwise return null
//         return null; // Placeholder, implement your validation logic
//     }

//     private String checkDuplicateData(Teacher teacher) {
//         // Check if the teacher already exists in the teacherMap
//         boolean exists = teacherMap.values().stream()
//                 .anyMatch(data ->
//                         teacher.getEmail().equals(data.getEmail()) ||
//                         teacher.getNip().equals(data.getNip()) ||
//                         teacher.getPhoneNumber().equals(data.getPhoneNumber())
//                 );

//         if (exists) {
//             return "Teacher with the same NIP, Email, or Phone Number already exists";
//         }

//         return null;
//     }

// 	@GetMapping(value = "/teachers/{nip}")
//     public ResponseEntity<Teacher> findTeacher(@PathVariable("nip") String nip) {
//         final Teacher teacher = teacherMap.get(nip);
//         return new ResponseEntity<>(teacher, HttpStatus.OK);
//     }

// 	private static List<Teacher> fetchTeachers() {
//         return teacherMap.values().stream().toList();
//     }

// 	@PostMapping(value = "/teachers/{nip}")
//     public String updateTeacher(@PathVariable("nip") String nip,
//                                 Teacher teacher,
//                                 BindingResult result, Model model) {
//         final Teacher teacherToBeUpdated = teacherMap.get(teacher.getNip());
//         teacherToBeUpdated.setFullName(teacher.getFullName());
//         teacherToBeUpdated.setEmail(teacher.getEmail());
//         teacherToBeUpdated.setPhoneNumber(teacher.getPhoneNumber());
//         teacherMap.put(teacher.getNip(), teacherToBeUpdated);

//         model.addAttribute("teachers", fetchTeachers());
//         return "redirect:/teachers";
//     }







//     @GetMapping(value = "/courses/{coursesCode}")
//     public ResponseEntity<Courses> findCourses(@PathVariable("codeCourses") String coursesCode) {
//         final Courses courses = coursesMap.get(coursesCode);
//         return new ResponseEntity<>(courses, HttpStatus.OK);
//     }
//     @PutMapping(value = "/courses/{coursesCode}")
//     public ResponseEntity<String> updateCourses(@PathVariable("coursesCode") String coursesCode,
//             @RequestBody Courses courses) {
//         final Courses coursesToBeUpdated = coursesMap.get(courses.getcoursesCode());
//         coursesToBeUpdated.setcoursesName(courses.getcoursesName());
//         coursesToBeUpdated.setfaculty(courses.getfaculty());
//         coursesToBeUpdated.setsumSKS(courses.getsumSKS());

//         coursesMap.put(courses.getcoursesCode(), coursesToBeUpdated);
//         return new ResponseEntity<>("Courses with Code: " + coursesToBeUpdated.getcoursesCode() +
//                 "has been updated", HttpStatus.OK);
//     }
//     @DeleteMapping(value = "/courses/{coursesCode}")
//     public ResponseEntity<Void> deleteCourses(@PathVariable("coursesCode") String coursesCode) {
//         coursesMap.remove(coursesCode);
//         return new ResponseEntity<Void>(HttpStatus.OK);
//     }
// }

