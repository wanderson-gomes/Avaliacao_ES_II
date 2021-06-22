package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RelatorioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Relatorio.class);
        Relatorio relatorio1 = new Relatorio();
        relatorio1.setId(1L);
        Relatorio relatorio2 = new Relatorio();
        relatorio2.setId(relatorio1.getId());
        assertThat(relatorio1).isEqualTo(relatorio2);
        relatorio2.setId(2L);
        assertThat(relatorio1).isNotEqualTo(relatorio2);
        relatorio1.setId(null);
        assertThat(relatorio1).isNotEqualTo(relatorio2);
    }
}
