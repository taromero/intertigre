package extension.custom

import org.spockframework.runtime.AbstractRunListener
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo
import org.spockframework.runtime.model.FeatureInfo

class ReportExtension extends AbstractAnnotationDrivenExtension<Report> {
    @Override
    void visitSpecAnnotation(Report annotation, SpecInfo spec) {
        spec.addListener(new AbstractRunListener() {
            @Override
            void afterFeature(FeatureInfo feature) {
				def lastKind = ''
                for (block in feature.blocks) {
					def aux = block
                    for (text in block.texts) {
						def bloque = lastKind.equals(block.kind.toString()) ? 'AND' : block.kind.toString()
						bloque =  traducir(bloque)
                        println bloque + ' ' + text
						lastKind = block.kind.toString()
                    }
                }
            }
        })
    }
	
	private traducir(bloque) {
		if(bloque == 'SETUP'){
			return 'DADO:    '
		} else if(bloque == 'WHEN') {
			return 'CUANDO:  '
		} else if(bloque == 'THEN') {
			return 'ENTONCES:'
		} else if(bloque == 'AND') {
			return 'Y:       '
		} else if(bloque == 'WHERE') {
			return 'DONDE:   '
		}
	}

}
