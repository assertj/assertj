package org.example.custom.assertions

import org.assertj.core.api.Assertions
import org.example.custom.assertions.domain.DomainObject
import org.example.custom.assertions.domain.assertions.DomainObjectAssertion
import org.example.custom.assertions.domain.assertions.ListDomainObjectsAssertion

class DomainAssertions extends Assertions {

    static DomainObjectAssertion assertThat(DomainObject actual) {
        new DomainObjectAssertion(actual)
    }

    static ListDomainObjectsAssertion assertThatDomainObjects(List<DomainObject> actual) {
        new ListDomainObjectsAssertion(actual)
    }

}
