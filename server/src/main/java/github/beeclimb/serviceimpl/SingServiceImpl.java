package github.beeclimb.serviceimpl;

import github.beeclimb.Sing;
import github.beeclimb.SingService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SingServiceImpl implements SingService {

    static {
        System.out.println("SingServiceImpl class had been init");
    }

    @Override
    public String sing(Sing sing) {
        log.info("SingServiceImpl receive: [{}]", sing.getSinger());
        String result = sing.getSinger() + "is singing" + sing.getSong();
        log.info("SingServiceImpl return: [{}]", result);
        return result;
    }

}
