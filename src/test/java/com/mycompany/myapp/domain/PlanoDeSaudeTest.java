package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanoDeSaudeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanoDeSaude.class);
        PlanoDeSaude planoDeSaude1 = new PlanoDeSaude();
        planoDeSaude1.setId(1L);
        PlanoDeSaude planoDeSaude2 = new PlanoDeSaude();
        planoDeSaude2.setId(planoDeSaude1.getId());
        assertThat(planoDeSaude1).isEqualTo(planoDeSaude2);
        planoDeSaude2.setId(2L);
        assertThat(planoDeSaude1).isNotEqualTo(planoDeSaude2);
        planoDeSaude1.setId(null);
        assertThat(planoDeSaude1).isNotEqualTo(planoDeSaude2);
    }
}
