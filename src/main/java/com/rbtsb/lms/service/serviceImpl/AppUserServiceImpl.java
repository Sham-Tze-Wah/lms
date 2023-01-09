//package com.rbtsb.lms.service.serviceImpl;
//
//import com.rbtsb.lms.constant.Role;
//import com.rbtsb.lms.dao.AppUserDao;
//import com.rbtsb.lms.dto.LoginDTO;
//import com.rbtsb.lms.entity.AppUserEntity;
//import com.rbtsb.lms.entity.PasswordResetTokenEntity;
//import com.rbtsb.lms.entity.RoleEntity;
//import com.rbtsb.lms.entity.VerificationTokenEntity;
//import com.rbtsb.lms.pojo.AppUserPojo;
//import com.rbtsb.lms.pojo.PasswordResetTokenPojo;
//import com.rbtsb.lms.pojo.RolePojo;
//import com.rbtsb.lms.pojo.VerificationTokenPojo;
//import com.rbtsb.lms.repo.AppUserRepo;
//import com.rbtsb.lms.repo.PasswordResetTokenRepo;
//import com.rbtsb.lms.repo.RoleRepo;
//import com.rbtsb.lms.repo.VerificationTokenRepo;
//import com.rbtsb.lms.service.AppUserService;
//import com.rbtsb.lms.service.mapper.*;
//import com.rbtsb.lms.util.validation.AppUserPojoValidation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.*;
//
//public class AppUserServiceImpl implements AppUserService {
//
//    @Autowired
//    private AppUserRepo appUserRepo;
//
//    @Autowired
//    private VerificationTokenRepo verificationTokenRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private PasswordResetTokenRepo passwordResetTokenRepository;
//
//    @Autowired
//    private RoleRepo roleRepo;
//
//    @Override
//    public LoginDTO registerUser(LoginDTO loginDTO) {
//        AppUserEntity appUserEntity = new AppUserEntity();
//        List<RoleEntity> roleEntityList = new ArrayList<>();
//        //AppUserPojo validatedAppUserPojo = AppUserPojoValidation.validation(loginDTO);
//
//        // TODO validation
//        appUserEntity.setUsername(loginDTO.getUsername());
//        appUserEntity.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
//        //appUserEntity.setMatchingPassword();
//        RolePojo rolePojo = roleRepo.findByRoleName("USER"); //Role.Employee.toString()
//        RoleEntity roleEntity = RoleMapper.pojoToEntity(rolePojo);
//        roleEntityList.add(roleEntity);
//        appUserEntity.setRoles(roleEntityList);
//        //appUserEntity.setEmployeeEntity();
//
//        appUserRepo.save(appUserEntity);
//        return loginDTO;
//    }
//
//    @Override
//    public void saveVerificationTokenForUser(String token, LoginDTO loginDTO) {
//        AppUserEntity user = LoginDTOMapper.dtoToEntity(loginDTO);
//
//        VerificationTokenEntity verificationToken =
//                new VerificationTokenEntity(user, token);
//
//        verificationTokenRepository.save(verificationToken);
//    }
//
//    @Override
//    public String validateVerificationToken(String token) {
//        VerificationTokenPojo verificationToken = verificationTokenRepository
//                .findByToken(token);
//
//        if(verificationToken == null) {
//            return "invalid";
//        }
//
//        AppUserPojo user = verificationToken.getAppUserPojo();
//        Calendar cal = Calendar.getInstance();
//
//        if(verificationToken.getExpirationTime().getTime() -
//                cal.getTime().getTime() <= 0){
//            VerificationTokenEntity tokenEntity = VerificationTokenMapper.pojoToEntity(verificationToken);
//            verificationTokenRepository.delete(tokenEntity);
//            return "expired";
//        }
//
//        user.setEnabled(true);
//        appUserRepo.save(AppUserMapper.pojoToEntity(user));
//        return "valid";
//    }
//
//    @Override
//    public VerificationTokenPojo generateNewVerificationToken(String oldToken) {
//        VerificationTokenPojo verificationToken =
//                verificationTokenRepository.findByToken(oldToken);
//
//        verificationToken.setToken(UUID.randomUUID().toString());
//        verificationTokenRepository.save(VerificationTokenMapper.pojoToEntity(verificationToken));
//        return verificationToken;
//    }
//
//    @Override
//    public AppUserPojo findUserByEmail(String email) {
//        return appUserRepo.findByUsername(email);
//    }
//
//    @Override
//    public void createPasswordResetTokenForUser(AppUserPojo user, String token) {
//        AppUserEntity appUserEntity = AppUserMapper.pojoToEntity(user);
//        PasswordResetTokenEntity passwordResetToken = new PasswordResetTokenEntity(appUserEntity, token);
//        passwordResetTokenRepository.save(passwordResetToken);
//    }
//
//    @Override
//    public String validatePasswordResetToken(String token) {
//        PasswordResetTokenPojo passwordResetToken =
//                passwordResetTokenRepository.findByToken(token);
//
//        if(passwordResetToken == null) {
//            return "invalid";
//        }
//
//
//        AppUserPojo user = passwordResetToken.getAppUserPojo();
//        Calendar cal = Calendar.getInstance();
//
//        if(passwordResetToken.getExpirationTime().getTime() -
//                cal.getTime().getTime() <= 0){
//            PasswordResetTokenEntity passwordResetTokenEntity = PasswordResetTokenMapper.pojoToEntity(passwordResetToken);
//            passwordResetTokenRepository.delete(passwordResetTokenEntity);
//            return "expired";
//        }
//
//        return "valid";
//    }
//
//    @Override
//    public Optional<AppUserPojo> getUserByPasswordResetToken(String token) {
//        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getAppUserPojo());
//    }
//
//    @Override
//    public void changePassword(AppUserPojo appUser, String newPassword) {
//        appUser.setPassword(passwordEncoder.encode(newPassword));
//        AppUserEntity appUserEntity = AppUserMapper.pojoToEntity(appUser);
//        appUserRepo.save(appUserEntity);
//    }
//
//    @Override
//    public boolean checkIfValidOldPassword(AppUserPojo appUser, String oldPassword) {
//        return passwordEncoder.matches(oldPassword, appUser.getPassword());
//    }
//}
