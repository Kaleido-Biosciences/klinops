package com.kaleido.klinops.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.kaleido.klinops.domain.ClinicalStudy;
import com.kaleido.klinops.domain.*; // for static metamodels
import com.kaleido.klinops.repository.ClinicalStudyRepository;
import com.kaleido.klinops.service.dto.ClinicalStudyCriteria;

/**
 * Service for executing complex queries for {@link ClinicalStudy} entities in the database.
 * The main input is a {@link ClinicalStudyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClinicalStudy} or a {@link Page} of {@link ClinicalStudy} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClinicalStudyQueryService extends QueryService<ClinicalStudy> {

    private final Logger log = LoggerFactory.getLogger(ClinicalStudyQueryService.class);

    private final ClinicalStudyRepository clinicalStudyRepository;

    public ClinicalStudyQueryService(ClinicalStudyRepository clinicalStudyRepository) {
        this.clinicalStudyRepository = clinicalStudyRepository;
    }

    /**
     * Return a {@link List} of {@link ClinicalStudy} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClinicalStudy> findByCriteria(ClinicalStudyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClinicalStudy> specification = createSpecification(criteria);
        return clinicalStudyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ClinicalStudy} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClinicalStudy> findByCriteria(ClinicalStudyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClinicalStudy> specification = createSpecification(criteria);
        return clinicalStudyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClinicalStudyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClinicalStudy> specification = createSpecification(criteria);
        return clinicalStudyRepository.count(specification);
    }

    /**
     * Function to convert {@link ClinicalStudyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClinicalStudy> createSpecification(ClinicalStudyCriteria criteria) {
        Specification<ClinicalStudy> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ClinicalStudy_.id));
            }
            if (criteria.getStudyIdentifier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStudyIdentifier(), ClinicalStudy_.studyIdentifier));
            }
            if (criteria.getPhase() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhase(), ClinicalStudy_.phase));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), ClinicalStudy_.status));
            }
            if (criteria.getSequence() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSequence(), ClinicalStudy_.sequence));
            }
            if (criteria.getStudyYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStudyYear(), ClinicalStudy_.studyYear));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ClinicalStudy_.name));
            }
            if (criteria.getDesign() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDesign(), ClinicalStudy_.design));
            }
            if (criteria.getNumberOfCohorts() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfCohorts(), ClinicalStudy_.numberOfCohorts));
            }
            if (criteria.getIntendedSubjectsPerCohort() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIntendedSubjectsPerCohort(), ClinicalStudy_.intendedSubjectsPerCohort));
            }
            if (criteria.getPopulationDiseaseState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPopulationDiseaseState(), ClinicalStudy_.populationDiseaseState));
            }
            if (criteria.getMinimumAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinimumAge(), ClinicalStudy_.minimumAge));
            }
            if (criteria.getMaximumAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaximumAge(), ClinicalStudy_.maximumAge));
            }
            if (criteria.getSubjectsEnrolled() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubjectsEnrolled(), ClinicalStudy_.subjectsEnrolled));
            }
            if (criteria.getFemalesEligible() != null) {
                specification = specification.and(buildSpecification(criteria.getFemalesEligible(), ClinicalStudy_.femalesEligible));
            }
            if (criteria.getMalesEligible() != null) {
                specification = specification.and(buildSpecification(criteria.getMalesEligible(), ClinicalStudy_.malesEligible));
            }
            if (criteria.getStudyShortName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStudyShortName(), ClinicalStudy_.studyShortName));
            }
            if (criteria.getProjectManager() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProjectManager(), ClinicalStudy_.projectManager));
            }
            if (criteria.getPrincipalPhysician() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrincipalPhysician(), ClinicalStudy_.principalPhysician));
            }
            if (criteria.getResearchRepresentative() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResearchRepresentative(), ClinicalStudy_.researchRepresentative));
            }
            if (criteria.getAnalysisRepresentative() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAnalysisRepresentative(), ClinicalStudy_.analysisRepresentative));
            }
            if (criteria.getDataManager() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataManager(), ClinicalStudy_.dataManager));
            }
            if (criteria.getMasterFileId() != null) {
                specification = specification.and(buildSpecification(criteria.getMasterFileId(),
                    root -> root.join(ClinicalStudy_.masterFile, JoinType.LEFT).get(TrialMasterFile_.id)));
            }
            if (criteria.getEndPointsId() != null) {
                specification = specification.and(buildSpecification(criteria.getEndPointsId(),
                    root -> root.join(ClinicalStudy_.endPoints, JoinType.LEFT).get(StudyEndPoint_.id)));
            }
            if (criteria.getStudiedProductsId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudiedProductsId(),
                    root -> root.join(ClinicalStudy_.studiedProducts, JoinType.LEFT).get(StudyProduct_.id)));
            }
            if (criteria.getMileStonesId() != null) {
                specification = specification.and(buildSpecification(criteria.getMileStonesId(),
                    root -> root.join(ClinicalStudy_.mileStones, JoinType.LEFT).get(StudyMilestone_.id)));
            }
            if (criteria.getBioAnalysesId() != null) {
                specification = specification.and(buildSpecification(criteria.getBioAnalysesId(),
                    root -> root.join(ClinicalStudy_.bioAnalyses, JoinType.LEFT).get(BioAnalysis_.id)));
            }
            if (criteria.getDataAnalysesId() != null) {
                specification = specification.and(buildSpecification(criteria.getDataAnalysesId(),
                    root -> root.join(ClinicalStudy_.dataAnalyses, JoinType.LEFT).get(DataAnalysis_.id)));
            }
            if (criteria.getShipmentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getShipmentsId(),
                    root -> root.join(ClinicalStudy_.shipments, JoinType.LEFT).get(Shipment_.id)));
            }
            if (criteria.getStudySamplesId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudySamplesId(),
                    root -> root.join(ClinicalStudy_.studySamples, JoinType.LEFT).get(StudySample_.id)));
            }
            if (criteria.getInvestigatorsId() != null) {
                specification = specification.and(buildSpecification(criteria.getInvestigatorsId(),
                    root -> root.join(ClinicalStudy_.investigators, JoinType.LEFT).get(PrincipalInvestigator_.id)));
            }
        }
        return specification;
    }
}
