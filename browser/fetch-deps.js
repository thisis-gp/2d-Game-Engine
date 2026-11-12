const fs=require('fs');const https=require('https');
const get=u=>new Promise((res,rej)=>https.get(u,r=>{if(r.statusCode!=200){r.resume();return res(null)}const c=[];r.on('data',d=>c.push(d));r.on('end',()=>res(Buffer.concat(c)))}).on('error',rej));
const seen=new Set();const jars=[];
async function visit(g,a,v,props0){
  const k=g+':'+a; if(seen.has(k))return; seen.add(k);
  const base=`https://repo1.maven.org/maven2/${g.replace(/\./g,'/')}/${a}/${v}/${a}-${v}`;
  const jar=await get(base+'.jar'); if(jar){fs.writeFileSync(`${a}-${v}.jar`,jar);jars.push(`${a}-${v}.jar`);}
  const pom=await get(base+'.pom'); if(!pom)return; const x=pom.toString();
  const props={...props0,'project.version':v,'project.groupId':g};
  for(const m of x.matchAll(/<([\w.\-]+)>([^<]*)<\/\1>/g)) if(!(m[1] in props)) props.__=1;
  const pm=x.match(/<properties>([\s\S]*?)<\/properties>/); if(pm)for(const m of pm[1].matchAll(/<([\w.\-]+)>([^<]*)</g))props[m[1]]=m[2];
  const sub=s=>s.replace(/\$\{([^}]+)\}/g,(_,n)=>props[n]??'');
  const depsec=x.replace(/<dependencyManagement>[\s\S]*?<\/dependencyManagement>/,'').replace(/<build>[\s\S]*<\/build>/,'').replace(/<profiles>[\s\S]*<\/profiles>/,'');
  for(const d of depsec.matchAll(/<dependency>([\s\S]*?)<\/dependency>/g)){
    const t=n=>(d[1].match(new RegExp(`<${n}>([^<]*)<`))||[])[1];
    const sc=t('scope')||'compile'; if(!['compile','runtime'].includes(sc)||t('optional')=='true')continue;
    let dv=sub(t('version')||''); if(!dv)continue;
    await visit(sub(t('groupId')),t('artifactId'),dv,props);
  }
}
(async()=>{
 const v='0.12.3';
 for(const a of ['teavm-tooling','teavm-classlib','teavm-jso','teavm-jso-apis','teavm-jso-impl','teavm-core','teavm-metaprogramming-impl','teavm-interop','teavm-platform','teavm-relocated-libs-commons-cli'])await visit('org.teavm',a,v,{});
 await visit('org.joml','joml','1.9.23',{});
 console.log(jars.join('\n'));
})();
