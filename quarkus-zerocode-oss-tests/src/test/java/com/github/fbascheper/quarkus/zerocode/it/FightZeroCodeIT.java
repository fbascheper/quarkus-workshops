package com.github.fbascheper.quarkus.zerocode.it;

import com.github.fbascheper.quarkus.zerocode.extension.httpclient.ProxySupportingHttpClient;
import org.jsmart.zerocode.core.domain.Scenario;
import org.jsmart.zerocode.core.domain.TargetEnv;
import org.jsmart.zerocode.core.domain.UseHttpClient;
import org.jsmart.zerocode.core.runner.ZeroCodeUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Complex Zero Code test.
 *
 * @author Erik-Berndt Scheper
 * @since 01-12-2020
 */
@TargetEnv("application-host.properties")
@RunWith(ZeroCodeUnitRunner.class)
@UseHttpClient(ProxySupportingHttpClient.class)
public class FightZeroCodeIT {

    @Test
    @Scenario("02-kafka/02-01-fight-hero-vs-villain.yml")
    public void testFightScenario() { // NOSONAR
        // No code is needed here.
    }

}
