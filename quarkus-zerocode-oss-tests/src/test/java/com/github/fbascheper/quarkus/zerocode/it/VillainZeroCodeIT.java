package com.github.fbascheper.quarkus.zerocode.it;

import org.jsmart.zerocode.core.domain.Scenario;
import org.jsmart.zerocode.core.domain.TargetEnv;
import org.jsmart.zerocode.core.runner.ZeroCodeUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * quarkus-zerocode-oss-tests - Description.
 *
 * @author Erik-Berndt Scheper
 * @since 01-12-2020
 */
@TargetEnv("application-host.properties")
@RunWith(ZeroCodeUnitRunner.class)
//@UseHttpClient(ProxySupportingHttpClient.class)
public class VillainZeroCodeIT {

    @Test
    @Scenario("01-simple/01-01-REST-get-villain-597.yml")
//    @Ignore
    public void testSimpleRestService() { // NOSONAR
        // No code is needed here.
    }

    @Test
    @Scenario("01-simple/01-02-Database-test-villain-597.yml")
//    @Ignore
    public void testSimpleDatabaseStep() { // NOSONAR
        // No code is needed here.
    }

}
