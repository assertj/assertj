package org.example.custom.assertions.domain.assertions

import org.assertj.core.api.AbstractListAssert
import org.assertj.core.presentation.PredicateDescription
import org.example.custom.assertions.domain.DomainObject

import static org.assertj.core.util.Lists.newArrayList

class ListDomainObjectsAssertion extends AbstractListAssert<ListDomainObjectsAssertion, List<DomainObject>, DomainObject, DomainObjectAssertion> {

    ListDomainObjectsAssertion(List<DomainObject> domainObjects) {
        super(domainObjects, ListDomainObjectsAssertion)
    }

    /**
     * Synthetic example
     */
    ListDomainObjectsAssertion hasExpectedDomainObjects() {
        isNotEmpty()

        writableAssertionInfo.description('All domain object must have value less 10')

        iterables.assertAllMatch(writableAssertionInfo, actual, { (it as DomainObject).value < 10 }, PredicateDescription.GIVEN)

        this
    }

    @Override
    protected DomainObjectAssertion toAssert(DomainObject value, String description) {
        toAssertForProxy(value, description)
    }

    protected DomainObjectAssertion toAssertForProxy(DomainObject value, String description) {
        new DomainObjectAssertion(value).as(description)
    }

    @Override
    protected ListDomainObjectsAssertion newAbstractIterableAssert(Iterable<? extends DomainObject> iterable) {
        newAbstractIterableAssertForProxy(iterable)
    }

    protected ListDomainObjectsAssertion newAbstractIterableAssertForProxy(Iterable<? extends DomainObject> iterable) {
        new ListDomainObjectsAssertion(newArrayList(iterable))
    }

}
