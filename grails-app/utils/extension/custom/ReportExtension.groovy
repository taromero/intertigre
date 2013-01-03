package extension.custom

import java.lang.invoke.MethodHandleImpl.BindCaller.T

import org.spockframework.runtime.AbstractRunListener
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.IterationInfo
import org.spockframework.runtime.model.SpecInfo
import static org.apache.commons.lang.StringEscapeUtils.*

class ReportExtension extends AbstractAnnotationDrivenExtension<Report> {
	static List<List<String>> dataValues = new ArrayList<List<String>>()
	def static counter = 0
	
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
				feature.dataVariables.eachWithIndex { elem, i -> i == feature.dataVariables.size()-1 ? print(elem) : print(elem + '    |    ') }
				println '\n'
				for(int i = 0; i < counter; i++) {
					def dataValue = dataValues.get(i)  
					dataValue.each {
						print it + '    '
					}
					print '\n'
				}
				dataValues = new ArrayList<List<String>>()
				counter = 0
            }
			
			@Override
			void afterIteration(IterationInfo iteration) {
				dataValues.add(new ArrayList<String>())
				iteration.dataValues.each {
					dataValues.get(counter).add(it.toString())
				}
				counter++
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
