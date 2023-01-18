package com.rbtsb.lms.service.serviceImpl;

import com.rbtsb.lms.constant.Role;
import com.rbtsb.lms.dao.AppUserDao;
import com.rbtsb.lms.dto.LoginDTO;
import com.rbtsb.lms.entity.*;
import com.rbtsb.lms.pojo.AppUserPojo;
import com.rbtsb.lms.pojo.PasswordResetTokenPojo;
import com.rbtsb.lms.pojo.RolePojo;
import com.rbtsb.lms.pojo.VerificationTokenPojo;
import com.rbtsb.lms.repo.*;
import com.rbtsb.lms.service.AppUserService;
import com.rbtsb.lms.service.mapper.*;
import com.rbtsb.lms.util.JwtUtils;
import com.rbtsb.lms.util.validation.AppUserPojoValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    @Autowired
    private AppUserRepo appUserRepo;

//    @Autowired
//    private UserRolesRepo userRolesRepo;

    @Autowired
    private VerificationTokenRepo verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepository;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public AppUserPojo registerUser(LoginDTO loginDTO) {
        AppUserEntity appUserEntity = new AppUserEntity();
        Set<RoleEntity> roleEntityList = new HashSet<>();
        //AppUserPojo validatedAppUserPojo = AppUserPojoValidation.validation(loginDTO);

        // TODO validation
        AppUserEntity appUserEntity1 = appUserRepo.findByEmail(loginDTO.getUsername());
        if(appUserEntity1 == null){
            appUserEntity.setUsername(loginDTO.getUsername());
            appUserEntity.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
            appUserEntity.setMatchingPassword(passwordEncoder.encode(loginDTO.getMatchingPassword()));

            RolePojo rolePojo = roleRepo.findByRoleName("USER"); //Role.Employee.toString() //TODO Checking on the proper role
            RoleEntity roleEntity = RoleMapper.pojoToEntity(rolePojo);
            roleEntityList.add(roleEntity);

            appUserEntity.setRoles(roleEntityList);

            Optional<EmployeeEntity> emp = employeeRepo.findByEmail(loginDTO.getUsername());
            if (emp.isPresent()) {
                appUserEntity.setEmployeeEntity(emp.get());
                //appUserEntity.setEmployeeEntity();

                appUserEntity.setPassword(appUserEntity.getPassword());
                appUserRepo.save(appUserEntity);
                AppUserPojo appUserPojo = AppUserMapper.entityToPojo(appUserEntity);
                return appUserPojo;
            } else {
                throw new NullPointerException("username is not found.");
            }
        }
        else{
            throw new NullPointerException("username is not found.");
        }
    }

    @Override
    public void saveVerificationTokenForUser(String token, AppUserPojo appUserPojo) {
        AppUserEntity user = AppUserMapper.pojoToEntity(appUserPojo);

        VerificationTokenEntity verificationToken =
                new VerificationTokenEntity(user, token);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationTokenEntity verificationTokenEntity = verificationTokenRepository.findByToken(token);
        if (verificationTokenEntity != null) {
            VerificationTokenPojo verificationToken = VerificationTokenMapper.entityToPojo(verificationTokenEntity);

            if (verificationToken == null) {
                return "invalid";
            }

            AppUserPojo user = verificationToken.getAppUserPojo();
            Calendar cal = Calendar.getInstance();

            if (verificationToken.getExpirationTime().getTime() -
                    cal.getTime().getTime() <= 0) {
                VerificationTokenEntity tokenEntity = VerificationTokenMapper.pojoToEntity(verificationToken);
                verificationTokenRepository.delete(tokenEntity);
                return "expired";
            }

            user.setEnabled(true);
            appUserRepo.save(AppUserMapper.pojoToEntity(user));
            return "valid";
        } else {
            throw new NullPointerException("the token provided is invalid. Please get another token.");
        }
    }

    @Override
    public String validateJwtToken(String token) {
        VerificationTokenEntity verificationTokenEntity = verificationTokenRepository.findByToken(token);
        if (verificationTokenEntity != null) {
            VerificationTokenPojo verificationToken = VerificationTokenMapper.entityToPojo(verificationTokenEntity);

            if (verificationToken == null) {
                return "invalid";
            }

            AppUserPojo user = verificationToken.getAppUserPojo();
            Calendar cal = Calendar.getInstance();

            if (verificationToken.getExpirationTime().getTime() -
                    cal.getTime().getTime() <= 0) {
                VerificationTokenEntity tokenEntity = VerificationTokenMapper.pojoToEntity(verificationToken);
                verificationTokenRepository.delete(tokenEntity);
                return "expired";
            } else if (!user.getUsername().equalsIgnoreCase(jwtUtils.extractUsername(token))) {
                return "token not belongs to you.";
            }

            user.setEnabled(true);
            appUserRepo.save(AppUserMapper.pojoToEntity(user));
            return "valid";
        } else {
            throw new NullPointerException("the token provided is invalid. Please get another token.");
        }
    }

    @Override
    public VerificationTokenPojo generateNewVerificationToken(String oldToken) {
        VerificationTokenPojo verificationToken =
                VerificationTokenMapper.entityToPojo(verificationTokenRepository.findByToken(oldToken));

        String username = jwtUtils.extractUsername(oldToken);
        AppUserEntity appUserEntity = appUserRepo.findByUsername(username);

        if (appUserEntity != null) {
            UserDetails user = AppUserMapper.entityToUserDetails(appUserEntity);
            String newToken = jwtUtils.generateToken(user);
            verificationToken.setToken(newToken);
            verificationTokenRepository.save(VerificationTokenMapper.pojoToEntity(verificationToken));
            return verificationToken;
        } else {
            throw new NullPointerException("cannot resend verification token");
        }
    }

    @Override
    public AppUserPojo findByUsername(String email) {
        AppUserEntity appUserEntity = appUserRepo.findByUsername(email);
        AppUserPojo appUserPojo = AppUserMapper.entityToPojo(appUserEntity);
        return appUserPojo;
    }

    @Override
    public AppUserPojo findByEmailAndRoleName(String email, String roleName) {
        AppUserEntity appUserEntity = appUserRepo.findByUsernameAndRoleName(email, roleName);
        AppUserPojo appUserPojo = AppUserMapper.entityToPojo(appUserEntity);
        return appUserPojo;
    }

    @Override
    public void createPasswordResetTokenForUser(AppUserPojo user, String token) {
        AppUserEntity appUserEntity = AppUserMapper.pojoToEntity(user);
        PasswordResetTokenEntity passwordResetToken = new PasswordResetTokenEntity(appUserEntity, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetTokenEntity passwordResetTokenEntity =
                passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return "invalid";
        }


        AppUserEntity user = passwordResetTokenEntity.getAppUserEntity();
        Calendar cal = Calendar.getInstance();

        if (passwordResetTokenEntity.getExpirationTime().getTime() -
                cal.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetTokenEntity);
            return "expired";
        }

        PasswordResetTokenPojo passwordResetTokenPojo = PasswordResetTokenMapper.entityToPojo(passwordResetTokenEntity);
        return "valid";
    }

    @Override
    public Optional<AppUserPojo> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(AppUserMapper.entityToPojo(passwordResetTokenRepository.findByToken(token).getAppUserEntity()));
    }

    @Override
    public void changePassword(AppUserPojo appUser, String newPassword) {
        appUser.setPassword(passwordEncoder.encode(newPassword));
        AppUserEntity appUserEntity = AppUserMapper.pojoToEntity(appUser);
        appUserRepo.save(appUserEntity);
    }

    @Override
    public boolean checkIfValidOldPassword(AppUserPojo appUser, String oldPassword) {
        return passwordEncoder.matches(oldPassword, appUser.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null) {
            throw new UsernameNotFoundException("username cannot be null.");
        }
        log.info("Start to initialize user details service");
        AppUserEntity appUserEntity = appUserRepo.findByEmail(email);
        log.info("App user entity might be null: " + email + "@" + appUserEntity);
        if (appUserEntity != null) {
            UserDetails user = AppUserMapper.entityToUserDetails(appUserEntity);
            log.info("Try to get user: " + user.toString());
            if (user == null) {
                throw new UsernameNotFoundException("conversion of user entity failed.");
            }
            List<SimpleGrantedAuthority> grantedAuthorities = user.getAuthorities().stream().
                    map(authority -> new SimpleGrantedAuthority(authority.toString())).
                    collect(Collectors.toList());
            return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("Something wrong with your credential or token.");
        }
    }
}
