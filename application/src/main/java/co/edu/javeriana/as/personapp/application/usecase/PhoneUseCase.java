package co.edu.javeriana.as.personapp.application.usecase;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;

import java.util.List;

public class PhoneUseCase implements PhoneInputPort {

    private PhoneOutputPort phoneOutputPort;
    public PhoneUseCase(PhoneOutputPort phoneOutputPort) {
        this.phoneOutputPort = phoneOutputPort;
    }

    @Override
    public void setPersistence(PhoneOutputPort phoneOutputPort) {
        this.phoneOutputPort = phoneOutputPort;
    }

    @Override
    public Phone create(Phone phone) {
        return phoneOutputPort.save(phone);
    }

    @Override
    public Phone edit(String number, Phone updatedPhone) throws NoExistException {
        Phone existingPhone = phoneOutputPort.findByNumber(number);
        if (existingPhone == null) {
            throw new NoExistException("Phone not found");
        }
        existingPhone.setNumber(updatedPhone.getNumber());
        existingPhone.setCompany(updatedPhone.getCompany());
        existingPhone.setOwner(updatedPhone.getOwner());
        return phoneOutputPort.save(existingPhone);
    }

    @Override
    public Boolean delete(String number) throws NoExistException {
        Phone phone = phoneOutputPort.findByNumber(number);
        if (phone == null) {
            throw new NoExistException("Phone not found");
        }
        phoneOutputPort.delete(number);
        return true;
    }

    @Override
    public List<Phone> findAll() {
        return phoneOutputPort.findAll();
    }

    @Override
    public Phone findByNumber(String number) throws NoExistException {
        Phone phone = phoneOutputPort.findByNumber(number);
        if (phone == null) {
            throw new NoExistException("Phone not found");
        }
        return phone;
    }

    @Override
    public Integer count() {
        return phoneOutputPort.count();
    }
}
