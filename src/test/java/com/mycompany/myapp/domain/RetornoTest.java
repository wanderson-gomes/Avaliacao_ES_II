package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetornoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Retorno.class);
        Retorno retorno1 = new Retorno();
        retorno1.setId(1L);
        Retorno retorno2 = new Retorno();
        retorno2.setId(retorno1.getId());
        assertThat(retorno1).isEqualTo(retorno2);
        retorno2.setId(2L);
        assertThat(retorno1).isNotEqualTo(retorno2);
        retorno1.setId(null);
        assertThat(retorno1).isNotEqualTo(retorno2);
    }
}
